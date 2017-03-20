<?php
class EpiCurl
{
  const timeout = 3;
  static $inst = null;
  static $singleton = 0;
  private $mc;
  private $msgs;
  private $running;
  private $requests = array();
  private $responses = array();
  private $properties = array();

  function __construct()
  {
	if(self::$singleton == 0)
	{
	  throw new Exception('This class cannot be instantiated by the new keyword.  You must instantiate it using: $obj = EpiCurl::getInstance();');
	}

	$this->mc = curl_multi_init();
	$this->properties = array(
	  'code'  => CURLINFO_HTTP_CODE,
	  'time'  => CURLINFO_TOTAL_TIME,
	  'length'=> CURLINFO_CONTENT_LENGTH_DOWNLOAD,
	  'type'  => CURLINFO_CONTENT_TYPE
	  );
  }

  public function addCurl($ch)
  {
	$key = (string)$ch;
	$this->requests[$key] = $ch;

	$res = curl_multi_add_handle($this->mc, $ch);

	// (1)
	if($res === CURLM_OK || $res === CURLM_CALL_MULTI_PERFORM)
	{
	  do {
		  $mrc = curl_multi_exec($this->mc, $active);
	  } while ($mrc === CURLM_CALL_MULTI_PERFORM);

	  return new EpiCurlManager($key);
	}
	else
	{
	  return $res;
	}
  }

  public function getResult($key = null)
  {
	if($key != null)
	{
	  if(isset($this->responses[$key]))
	  {
		return $this->responses[$key];
	  }

	  $running = null;
	  do
	  {
		$resp = curl_multi_exec($this->mc, $runningCurrent);
		if($running !== null && $runningCurrent != $running)
		{
		  $this->storeResponses($key);
		  if(isset($this->responses[$key]))
		  {
			return $this->responses[$key];
		  }
		}
		$running = $runningCurrent;
	  }while($runningCurrent > 0);
	}

	return false;
  }

  private function storeResponses()
  {
	while($done = curl_multi_info_read($this->mc))
	{
	  $key = (string)$done['handle'];
	  $this->responses[$key]['data'] = curl_multi_getcontent($done['handle']);
	  foreach($this->properties as $name => $const)
	  {
		$this->responses[$key][$name] = curl_getinfo($done['handle'], $const);
		curl_multi_remove_handle($this->mc, $done['handle']);
	  }
	}
  }

  static function getInstance()
  {
	if(self::$inst == null)
	{
	  self::$singleton = 1;
	  self::$inst = new EpiCurl();
	}

	return self::$inst;
  }
}

class EpiCurlManager
{
  private $key;
  private $epiCurl;

  function __construct($key)
  {
	$this->key = $key;
	$this->epiCurl = EpiCurl::getInstance();
  }

  function __get($name)
  {
	$responses = $this->epiCurl->getResult($this->key);
	return $responses[$name];
  }
}

/*
 * Credits:
 *  - (1) Alistair pointed out that curl_multi_add_handle can return CURLM_CALL_MULTI_PERFORM on success.
 */
?>
