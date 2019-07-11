###
# **************************************************-
# InGrid Portal Distribution
# ==================================================
# Copyright (C) 2014 - 2019 wemove digital solutions GmbH
# ==================================================
# Licensed under the EUPL, Version 1.1 or – as soon they will be
# approved by the European Commission - subsequent versions of the
# EUPL (the "Licence");
# 
# You may not use this work except in compliance with the Licence.
# You may obtain a copy of the Licence at:
# 
# http://ec.europa.eu/idabc/eupl5
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the Licence is distributed on an "AS IS" basis,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the Licence for the specific language governing permissions and
# limitations under the Licence.
# **************************************************#
###
#!/bin/bash
PROFILES_DIR="webapps/ingrid-portal-apps/profiles"
HIERARCHY_DIR="webapps/ingrid-portal-mdek-application/dojo-sources/ingrid/hierarchy"

cd /opt/ingrid/ingrid-portal/apache-tomcat

if [ -e /initialized ]
then
    echo "Container already initialized"
else
    sed -i 's/jdbc:mysql:\/\/localhost\/ingrid-portal?autoReconnect=true/'${DB_URL_PORTAL}'/' conf/Catalina/localhost/ingrid-portal-apps.xml
    sed -i 's/password=""/password="${DB_PASSWORD}"/' conf/Catalina/localhost/ingrid-portal-apps.xml
    sed -i 's/jdbc:mysql:\/\/localhost\/mdek/'${DB_URL_MDEK}'/' conf/Catalina/localhost/ingrid-portal-mdek.xml
    sed -i 's/password=""/password="${DB_PASSWORD}"/' conf/Catalina/localhost/ingrid-portal-mdek.xml
    sed -i 's/jdbc:mysql:\/\/localhost\/ingrid-portal?autoReconnect=true/'${DB_URL_PORTAL}'/' conf/Catalina/localhost/ROOT.xml
    sed -i 's/password=""/password="${DB_PASSWORD}"/' conf/Catalina/localhost/ROOT.xml
    sed -i 's/hibernate.driverClass=com.mysql.jdbc.Driver/hibernate.driverClass='${DB_DRIVERCLASS}'/' webapps/ingrid-portal-mdek-application/WEB-INF/classes/default-datasource.properties
    sed -i 's/hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect/hibernate.dialect='${DB_DIALECT}'/' webapps/ingrid-portal-mdek-application/WEB-INF/classes/default-datasource.properties
    sed -i 's/jdbc:mysql:\/\/localhost\/mdek/'${DB_URL_MDEK}'/' webapps/ingrid-portal-mdek-application/WEB-INF/classes/default-datasource.properties
    sed -i 's/hibernate.user=root/hibernate.user='${DB_USER}'/' webapps/ingrid-portal-mdek-application/WEB-INF/classes/default-datasource.properties
    sed -i 's/hibernate.password=/hibernate.password='${DB_PASSWORD}'/' webapps/ingrid-portal-mdek-application/WEB-INF/classes/default-datasource.properties

    sed -i 's/communications.ibus=\/ingrid-group\\:ibus-live,127.0.0.1,9900/communications.ibus=\/ingrid-group:ibus,'${IBUS_IP}',9900/' webapps/ingrid-portal-apps/WEB-INF/classes/ingrid-portal-apps.properties
    sed -i 's/portal.enable.caching=true/portal.enable.caching='${PORTAL_CACHE_ENABLE}'/' webapps/ingrid-portal-apps/WEB-INF/classes/ingrid-portal-apps.properties
    sed -i 's/csw.interface.url=https:\/\/dev.informationgrid.eu\/csw/csw.interface.url='${PORTAL_CSW_URL}'/' webapps/ingrid-portal-apps/WEB-INF/classes/ingrid-portal-apps.properties

    # handle portal profiles
    if [ "$PORTAL_PROFILE" ]; then
        echo "Using specific portal profile: $PORTAL_PROFILE"

        if [ ! -d "$PROFILES_DIR/$PORTAL_PROFILE" ]; then
            echo >&2 "PROFILE DIRECTORY NOT FOUND: '$PROFILES_DIR/$PORTAL_PROFILE'"
            exit 1
        fi

        # UVP-NI extends UVP
        if [ "$PORTAL_PROFILE" == "uvp-ni" ]; then
            echo "Copying profile files from parent (uvp) into portal directories ..."
            cp -R $PROFILES_DIR/uvp/ingrid-portal-mdek-application/* webapps/ingrid-portal-mdek-application
            cp -R $PROFILES_DIR/uvp/ingrid-webmap-client/* webapps/ingrid-webmap-client
        fi

        echo "Copying profile files into portal directories ..."
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-portal/* webapps/ROOT
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-portal-apps/* webapps/ingrid-portal-apps
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-portal-mdek/* webapps/ingrid-portal-mdek
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-portal-mdek-application/* webapps/ingrid-portal-mdek-application
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-webmap-client/* webapps/ingrid-webmap-client
        cp -R $PROFILES_DIR/$PORTAL_PROFILE/ingrid-webmap-client/frontend/src/* webapps/ingrid-webmap-client/frontend/prd

        echo "Copy repeatable SQL file for profile ..."
        cp $PROFILES_DIR/$PORTAL_PROFILE/profile_mysql.sql webapps/ROOT/WEB-INF/classes/db/migration/mysql/afterMigrate.sql
        cp $PROFILES_DIR/$PORTAL_PROFILE/profile_postgres.sql webapps/ROOT/WEB-INF/classes/db/migration/postgres/afterMigrate.sql
        cp $PROFILES_DIR/$PORTAL_PROFILE/profile_oracle.sql webapps/ROOT/WEB-INF/classes/db/migration/oracle/afterMigrate.sql

        # specific options for UVP
        if [ "$PORTAL_PROFILE" == "uvp" ]; then
            # deactivate behaviours
            find $HIERARCHY_DIR -not -path 'webapps/ingrid-portal-mdek-application/dojo-sources/ingrid/hierarchy/behaviours/uvp/*' -type f -name '*.js' -not -name 'folder*' -exec sed -i 's/defaultActive \?: \?true/defaultActive: false/' {} \;
            find $HIERARCHY_DIR -not -path 'webapps/ingrid-portal-mdek-application/dojo-sources/ingrid/hierarchy/behaviours/uvp/*' -type f -name '*.js' -not -name 'folder*' -exec sed -i 's/defaultActive \?: \?!0/defaultActive:0/' {} \;

            # increase session timeout to 120 minutes
            sed -i 's/<session-timeout>30<\/session-timeout>/<session-timeout>120<\/session-timeout>/' conf/web.xml
        fi

        # specific options for UVP-NI
        if [ "$PORTAL_PROFILE" == "uvp-ni" ]; then
            # deactivate behaviours
            find $HIERARCHY_DIR -not -path 'webapps/ingrid-portal-mdek-application/dojo-sources/ingrid/hierarchy/behaviours/uvp/*' -type f -name '*.js' -not -name 'folder*' -exec sed -i 's/defaultActive \?: \?true/defaultActive: false/' {} \;
            find $HIERARCHY_DIR -not -path 'webapps/ingrid-portal-mdek-application/dojo-sources/ingrid/hierarchy/behaviours/uvp/*' -type f -name '*.js' -not -name 'folder*' -exec sed -i 's/defaultActive \?: \?!0/defaultActive:0/' {} \;

            # increase session timeout to 120 minutes
            sed -i 's/<session-timeout>30<\/session-timeout>/<session-timeout>120<\/session-timeout>/' conf/web.xml
        fi

    else
        echo "No specific portal profile used."
    fi
    
    # IGE standalone configuration
    if [ "$STANDALONE_IGE" == "true" ]; then
        # remove all contexts except ingrid-portal-mdek-application
        rm -rf webapps/ingrid-portal-apps*
        rm -rf webapps/ingrid-webmap-client*
        rm -rf webapps/ingrid-portal-mdek.war
        rm -rf webapps/ingrid-portal-mdek/
        rm -rf webapps/ROOT*

        # remove context files
        rm conf/Catalina/localhost/ingrid-portal-apps.xml conf/Catalina/localhost/ingrid-portal-mdek.xml conf/Catalina/localhost/ingrid-webmap-client.xml conf/Catalina/localhost/ROOT.xml

        # adapt configuration use standalone version of IGE
        echo 'installation.standalone=true' >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties
        echo 'admin.password=admin' > webapps/ingrid-portal-mdek-application/WEB-INF/classes/igeAdminUser.properties
    fi

    # SMTP_HOST
    echo "workflow.mail.smtp=$SMTP_HOST" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties
    echo "workflow.mail.sender=$MAIL_SENDER" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties
    echo "workflow.mail.smtp.user=$MAIL_USER" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties
    echo "workflow.mail.smtp.password=$MAIL_PASSWORD" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties

    echo "mdek.directLink=$IGE_DIRECT_LINK" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties

    # Upload settings
    echo "upload.docsdir=$UPLOAD_DOCS_DIR" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties
    echo "upload.partsdir=$UPLOAD_PARTS_DIR" >> webapps/ingrid-portal-mdek-application/WEB-INF/classes/mdek.override.properties

    touch /initialized
fi

if [ "$DEBUG" = 'true' ]; then
    ./bin/catalina.sh jpda run
else
    ./bin/catalina.sh run
fi
