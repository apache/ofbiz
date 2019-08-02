# jquery.cookieBar plugin
[Go to demo page](https://cookiebar.pavelkovar.cz/)

## Introduction

Small jQuery plugin, which adds a simple Cookie Bar with info about cookies using. Plugin is simply modified and contains clever multilingual function.

## Installing

The simplest ways to download and install this plugin.

###Step 1: Install

#### Via npm
`$ npm i kovarp-jquery-cookiebar`

#### Clone repository
`$ git clone https://github.com/kovarp/jquery.cookieBar`

#### Direct download
Download this plugin direct in latest version from GitHub repository or from [the plugin page](http://cookiebar.pavelkovar.cz/)

###Step 2: Add to page

#### Link plugin CSS file in page head

```html
<link rel="stylesheet" href="jquery.cookieBar.min.css">
```

#### Link plugin script file after jQuery

```html
<script src="jquery.min.js"></script>
<script src="jquery.cookieBar.js"></script>
```
## Using

Examples of plugin using.

### Default initialization

```js
$(function() {
	$.cookieBar();
});
```

### Initialization with custom options

```js
$(function() {
	$.cookieBar({
		style: 'bottom'
	});
});
```

## Options

**style** - Define style of display cookie bar on page
```
default: 'top'
options: 'top', 'bottom', 'bottom-left', 'bottom-right'
```

**wrapper** - Wrapper, where the cookie bar will prepended
```
default: 'body'
options: string
```

**expireDays** - Number of days, when the cookies will expires
```
default: 365
options: integer
```

**infoLink** - URL for "more info link"
```
default: 'https://www.google.com/policies/technologies/cookies/'
options: string
```

**infoTarget** - Target attribute for "more info link"
```
default: '_blank'
options: '_blank', '_self', '_parent', '_top'
```

**language** - Language of the cookie bar
```
default: $('html').attr('lang') || 'en'
options: 'ISO 639-1 Language Code'
```
_The default value means, that the plugin detect automatically language from html lang attribute._

**privacy** - Show privacy protection button (GDPR)
```
default: false
options: false, 'popup', 'bs_modal', 'link'
```

**privacyTarget** - Target attribute for "privacy button link"
```
default: '_blank'
options: '_blank', '_self', '_parent', '_top'
```

**privacyContent** - Content for privacy button
```
default: null
options: null, 'custom HTML', 'Bootstrap modal ID', 'url'
```

## Languages support

You can simply add new language for Cookie Bar. Just use **addTranslation** method.

```js
$(function() {
	$.cookieBar('addTranslation', 'de', {
		message: 	'Zur Bereitstellung von Diensten verwenden wir Cookies. Durch die Nutzung dieser Website stimmen Sie zu.',
		acceptText:	'OK',
		infoText:	'Mehr Informationen'
	});
});
```

Then, you can initialize the Cookie Bar with your own language.

```js
$(function() {
	$.cookieBar({
		language: 'de'
	});
});
```

_Feel free to make pull request with your new language. ;)_

## Detect cookies state

You can detect, if user agreed with cookies using. The plugin save this information in cookie with name **cookies-state**. So, you can just read this value by JS or PHP and check, if the value equals **accepted**.
