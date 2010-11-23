rem To quickly populate the Funambol DB (DB must be created before)
ant -f opentaps/funambol/install/install.xml install
ant -f opentaps/funambol/install/install.xml install-sync-module
ant -f opentaps/funambol/install/install.xml install-sync-source