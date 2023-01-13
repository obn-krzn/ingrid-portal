<%--
  **************************************************-
  InGrid Portal Apps
  ==================================================
  Copyright (C) 2014 - 2023 wemove digital solutions GmbH
  ==================================================
  Licensed under the EUPL, Version 1.1 or – as soon they will be
  approved by the European Commission - subsequent versions of the
  EUPL (the "Licence");
  
  You may not use this work except in compliance with the Licence.
  You may obtain a copy of the Licence at:
  
  http://ec.europa.eu/idabc/eupl5
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the Licence is distributed on an "AS IS" basis,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the Licence for the specific language governing permissions and
  limitations under the Licence.
  **************************************************#
  --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Set the locale to the value of parameter 'lang' and init the message bundle messages.properties -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value='<%= request.getParameter("lang") == null ? "de" : request.getParameter("lang") %>' scope="session" />

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
    <head>
        <title>AdV-MIS</title>
        <link rel="shortcut icon" href="/decorations/layout/ingrid/images/favicon.ico">
        <link rel="prefetch" href="/decorations/layout/ingrid/images/template/icons.svg">
        <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta name="description" content="AdV-MIS bietet kostenlosen und werbefreien Zugang zu Informationen &ouml;ffentlicher Institutionen und Organisationen. " />
        <meta name="author" content="wemove digital solutions" />
        <meta name="keywords" lang="de" content="AdV-MIS, Umweltportal, Umweltinformationen, Deutschland, Bund, Bundesl&auml;nder, L&auml;nder, &ouml;ffentliche Institutionen, &ouml;ffentliche Organisationen, Suche, Recherche, werbefrei, kostenlos, Umweltdatenkataloge, Umwelt, UDK, Datenkataloge, Datenbanken" />
        <meta name="copyright" content="wemove digital solutions GmbH" />
        <meta name="robots" content="index,follow" />
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/main.css">
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/override.css">
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/print.css" media="print">

        <script src="/decorations/layout/ingrid/scripts/ingrid.js"></script>
        <!-- Global scripts -->
        <script src="/decorations/layout/ingrid/scripts/modernizr.custom.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/jquery-2.1.4.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/fastclick.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/jquery.nicescroll.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/foundation.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/select2.full.js"></script>
        <script src="/decorations/layout/ingrid/scripts/main.js"></script>
        <!-- bkg -->
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/bkg/normalize.css" type="text/css" media="screen">
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/bkg/small.css" type="text/css" media="screen">
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/bkg/medium.css" type="text/css" media="screen">
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/bkg/large.css" type="text/css" media="screen">
        <meta class="foundation-mq">
    </head>
    <body>
        <div class="container">
            <div class="header-menu" style="display: none; overflow: hidden; outline: none; cursor: grab; touch-action: none;" role="navigation" aria-label="Navigation Menu">
                <div class="header-menu-close">
                    <button type="button" class="button ">Menu<span class="ic-ic-cross"></span>
                    </button>
                </div>
                <div class="menu-main-links">
                    <div class="highlighted">
                        <a href="/freitextsuche" class="header-menu-entry " title="Freitextsuche nach Informationen"><span class="text">Suche</span></a>
                        <a href="/hintergrundinformationen" class="header-menu-entry " title="Hintergrundinformationen zu ADV-MIS"><span class="text">Über ADV-MIS</span></a>
                    </div>
                    <a href="/kontakt" title="Ihre Nachricht, Fragen oder Anregungen direkt an BKG-MIS"><span class="text">Kontakt</span></a>
                    <a href="/impressum" title="Inhaltlich und technisch Verantwortliche, Nutzungsbedingungen, Haftungsausschluss"><span class="text">Impressum</span></a>
                    <a href="https://www.adv-online.de/Datenschutzerklaerung/" title="Unsere Verpflichtung zum Umgang mit persönlichen Informationen"><span class="text" target="_blank">Datenschutz</span></a>
                    <a href="/barrierefreiheit" title="Erklärung zur Barrierefreiheit"><span class="text">Erklärung zur Barrierefreiheit</span></a>
                </div>
                <div class="menu-sub-links"></div>
            </div>
            <header>
                <div class="row">
                    <div class="columns xsmall-15 small-15 medium-15 large-14 xlarge-15">
                        <div class="row">
                          <div class="columns xsmall-20 small-17 medium-17 large-17 xlarge-10">
                            <div class="logo" role="banner">
                                <a target="_blank" href="http://www.adv-online.de" title="Startseite von AdV-MIS">
                                    <img src="/decorations/layout/ingrid/images/template/advmis_logo.png" alt="advmis_logo.png">
                                </a>
                                <div class="desktop__title">
                                    <a href="/startseite" title="Startseite von AdV-MIS aufrufen ">
                                        <div class="vertical-line"></div>
                                        <p>
                                            <span class="title-main">AdV</span>
                                            <br>
                                            <span class="title-sub">MetadatenInformationsSystem</span>
                                        </p>
                                    </a>
                                </div>
                            </div>
                          </div>
                        </div>
                    </div>
                    <div class="columns xsmall-9 small-7 medium-7 large-7 xlarge-6 nav-tabs">
                        <div class="menu-tab-row" role="navigation" aria-label="Navigation Header">
                            <a class="menu-tab " href="/freitextsuche" title="Freitextsuche nach Informationen">
                                <div class="link-menu-tab">
                                    <span class="ic-ic-lupe"></span>
                                    <span class="text">Suche</span>
                                </div>
                            </a>
                            <a class="menu-tab " href="/hintergrundinformationen" title="Hintergrundinformationen zu BKG-MIS">
                                <div class="link-menu-tab">
                                    <span class="ic-ic-info"></span>
                                    <span class="text">Über BKG-MIS</span>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="columns">
                        <div class="header-menu-open">
                            <button type="button" class="button xsmall-button">
                                <span class="ic-ic-hamburger"></span>
                            </button>
                            <button type="button" class="button small-button">
                                Menu<span class="ic-ic-hamburger"></span>
                            </button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="body">
                <div class="banner subpage">
                    <div class="subpage-wrapper" style="background-image: url('/decorations/layout/ingrid/images/template/drops-subpage.svg');">
                        <div class="row align-center">
                            <div class="large-20 columns dark">
                                <h1><fmt:message key="ui.entry.session.expired" /></h1>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row content-small">
                    <div class="columns">
                        <div class="form">
                            <p>
                               <fmt:message key="ui.entry.session.expired.text" />
                            </p>
                            <div class="link-list">
                                <a class="button" href="/log-in">
                                    <span class="text">Login</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer id="footer">
                <div id="social-media-footer" class="hide-for-small-only">
                  <div class="socialmedia">
                      <h2 class="aural">Service</h2>
                      <ul class="columns">
                        <li class="navAdvmis"><a class="external" href="http://www.adv-online.de/" title="Externer Link&nbsp;zur AdV Online Webseite (Öffnet&nbsp;neues&nbsp;Fenster)" target="_blank"><span>AdV-online</span></a></li>
                      </ul>
                  </div>
                </div>
                <div class="copyright row">
                  <div class="xsmall-6 small-4 medium-2 columns bkg-logo">
                    <a target="_blank" href="http://www.adv-online.de/" title="AdV Webseite">
                        <img src="/decorations/layout/ingrid/images/template/advmis_logo.png" alt="Logo">
                    </a>
                  </div>
                  <div class="legal-links small-18 medium-21 columns">
                    <div class="row">
                      <div class="columns hide-for-xsmall-only hide-for-small-only">
                          <ul class="footer-menu-entries" role="navigation" aria-label="Navigation Footer">
                              <li><a href="/kontakt" title="Ihre Nachricht, Fragen oder Anregungen direkt an AdV-MIS"><span class="text">Kontakt</span></a></li>
                              <li><a href="/impressum" title="Inhaltlich und technisch Verantwortliche, Nutzungsbedingungen, Haftungsausschluss"><span class="text">Impressum</span></a></li>
                              <li><a href="https://www.bkg.bund.de/DE/Service/Datenschutz/datenschutz.html" title="Unsere Verpflichtung zum Umgang mit persönlichen Informationen" target="\&quot;_blank\&quot;"><span class="text">Datenschutz</span></a></li>
                              <li><a href="/barrierefreiheit" title="Barrierefreiheit"><span class="text">Erklärung zur Barrierefreiheit</span></a></li>
                         </ul>
                      </div>
                    </div>
                    <div class="row">
                      <div class="columns">
                        <p class="copyright_text">© 2020 Arbeitsgemeinschaft der Vermessungsverwaltungen der Länder der Bundesrepublik Deutschland</p>
                      </div>
                    </div>
                  </div>
                </div>
            </footer>
        </div>
    </body>
    <script src="/decorations/layout/ingrid/scripts/all.js"></script>
    <script>
    // Add shrink class
        var body = $("body");
        
        if(window.scrollY > 100) {
            body.addClass("shrink");
        }
        window.onscroll = function(e) {
          if (this.scrollY > 100) {
            body.addClass("shrink");
          } else {
            body.removeClass("shrink");
          }
        };
    </script>
</html>
