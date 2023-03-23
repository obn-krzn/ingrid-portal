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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- Set the locale to the value of parameter 'lang' and init the message bundle messages.properties -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale
    value='<%=request.getParameter("lang") == null ? "de" : request.getParameter("lang")%>'
    scope="session" />

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="de">
    <head>
        <title>LUBW - Landesanstalt f&uuml;r Umwelt Baden-W&uuml;rttemberg</title>
        <meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta name="description" content="LUBW, die Landesanstalt f&uuml;r Umwelt Baden-W&uuml;rttemberg, bietet kostenlosen und werbefreien Zugang zu Umweltinformationen &ouml;ffentlicher Institutionen und Organisationen. " />
        <meta name="author" content="&copy; LUBW - Landesanstalt f&uuml;r Umwelt Baden-W&uuml;rttemberg" />
        <meta name="keywords" lang="de" content="LUBW, Umweltportal, Umweltinformationen, Deutschland, Bund, Bundesl&auml;nder, L&auml;nder, &ouml;ffentliche Institutionen, &ouml;ffentliche Organisationen, Suche, Recherche, werbefrei, kostenlos, Umweltdatenkataloge, Umwelt, UDK, Datenkataloge, Datenbanken" />
        <meta name="copyright" content="&copy; LUBW - Landesanstalt f&uuml;r Umwelt Baden-W&uuml;rttemberg" />
        <meta name="robots" content="index,follow" />
        <link rel="shortcut icon" href="/decorations/layout/ingrid/images/favicon.ico" />
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/main.css" />
        <link rel="stylesheet" href="/decorations/layout/ingrid/css/override.css" />
        <script src="/decorations/layout/ingrid/scripts/jquery-2.1.4.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/fastclick.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/jquery.nicescroll.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/foundation.min.js"></script>
        <script src="/decorations/layout/ingrid/scripts/vendor/select2.full.js"></script>
        <script src="/decorations/layout/ingrid/scripts/main.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="header-menu" style="display: none; overflow: hidden; outline: none;" tabindex="1">
                <div class="header-menu-close">
                    <button type="button" class="button ">Menü<span class="ic-ic-cross"></span></button>
                </div>
                <div class="menu-main-links">
                    <div class="highlighted">
                        <a href="/freitextsuche" class="header-menu-entry " title="Freitextsuche nach Umweltinformationen"><span class="text">Suche</span></a>
                        <a href="/kartendienste" class="header-menu-entry " title="Kartenansicht"><span class="text">Karten</span></a>
                        <a href="/themenuebersicht" class="header-menu-entry " title="Themenübersicht"><span class="text">Themenübersicht</span></a>
                        <a href="/hintergrundinformationen" class="header-menu-entry " title="Hintergrundinformationen zur LUBW"><span class="text">Über die LUBW</span></a>
                        <a href="/datenquellen" class="header-menu-entry " title="Angeschlossene Datenbanken"><span class="text">Datenquellen</span></a>
                    </div>
                    <a href="/hilfe" title="Erläuterungen zur Nutzung von LUBW - Neues Fenster öffnet sich"><span class="text">Hilfe</span></a>
                    <a href="/kontakt" title="Ihre Nachricht, Fragen oder Anregungen direkt an LUBW"><span class="text">Kontakt</span></a>
                    <a href="/inhaltsverzeichnis" title="Alle Inhalte von LUBW auf einen Blick"><span class="text">Inhalt</span></a>
                    <a href="/impressum" title="Inhaltlich und technisch Verantwortliche, Nutzungsbedingungen, Haftungsausschluss"><span class="text">Impressum</span></a>
                    <a href="/datenschutzbestimmung" title="Unsere Verpflichtung zum Umgang mit persönlichen Informationen"><span class="text">Datenschutz</span></a>
                    <a href="/barrierefreiheit" title="Er&auml;uterung zur Barrierefreiheit"><span class="text">Barrierefreiheit</span></a>
                </div>
            </div>
            <header>
                <div class="row">
                    <div class="columns xsmall-7 small-7 medium-8 large-7 xlarge-9">
                        <div class="logo">
                            <a href="/startseite" class="hide-for-xsmall-only hide-for-small-only">
                                <img src="/decorations/layout/ingrid/images/template/bw-wappen.png" alt="LUBW"/>
                                <div class="text">Landesanstalt für Umwelt<br>Baden-Württemberg</div>
                            </a>
                            <a href="/startseite')" class="hide-for-medium">
                                <img src="/decorations/layout/ingrid/images/template/bw-wappen.png" alt="LUBW"/>
                                <div class="text">Landesanstalt für Umwelt<br>Baden-Württemberg</div>
                            </a>
                        </div>
                    </div>
                    <div class="columns xsmall-14 small-15 medium-14 large-13 xlarge-10 nav-tabs">
                        <div class="menu-tab-row">
                            <a class="menu-tab" href="/freitextsuche" title="Freitextsuche nach Umweltinformationen">
                                <div class="link-menu-tab">
                                    <span class="ic-ic-lupe"></span>
                                    <span class="text">Suche</span>
                                </div>
                            </a>
                            <a class="menu-tab " href="/kartendienste" title="Kartenansicht">
                                <div class="link-menu-tab">
                                    <span class="ic-ic-karten"></span>
                                    <span class="text">Karten</span>
                                </div>
                            </a>
                            <a class="menu-tab " href="/themenuebersicht" title="Themenübersicht">
                                <div class="link-menu-tab">
                                    <span class="ic-ic-datenkataloge"></span>
                                    <span class="text">Themenübersicht</span>
                                </div>
                            </a>
                        </div>
                    </div>
                    <div class="columns">
                        <div class="header-menu-open">
                            <button type="button" class="button xsmall-button"><span class="ic-ic-hamburger"></span></button>
                            <button type="button" class="button small-button">Menü<span class="ic-ic-hamburger"></span></button>
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
                                <a class="icon" href="/log-in">
                                    <span class="ic-ic-arrow"></span>
                                    <span class="text">Login</span>
                                </a> 
                           </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer>
                <div class="footer">
                    <hr>
                    <div class="row">
                        <div class="xsmall-24 medium-24 large-24 xlarge-9 columns">
                            <div class="logo">
                                <a href="/startseite"><img class="footer__logo" src="/decorations/layout/ingrid/images/template/bw-wappen.png" alt="LUBW"/></a>
                            </div>
                            <div class="copyright">
                                <span class="icon"></span>
                                <span class="text copyright_text">LUBW - Landesanstalt für Umwelt Baden-Württemberg</span>
                            </div>
                        </div>
                        <div class="xsmall-24 small-24 large-24 xlarge-15 columns">
                            <div class="footer-menu-entries">
                                <a href="/hilfe" title="Erläuterungen zur Nutzung von LUBW - Neues Fenster öffnet sich" class="icon"><span class="text">Hilfe</span></a>
                                <a href="/kontakt" title="Ihre Nachricht, Fragen oder Anregungen direkt an LUBW" class="icon"><span class="text">Kontakt</span></a>
                                <a href="/inhaltsverzeichnis" title="Alle Inhalte von LUBW auf einen Blick" class="icon"><span class="text">Inhalt</span></a>
                                <a href="/impressum" title="Inhaltlich und technisch Verantwortliche, Nutzungsbedingungen, Haftungsausschluss" class="icon"><span class="text">Impressum</span></a>
                                <a href="/datenschutzbestimmung" title="Unsere Verpflichtung zum Umgang mit persönlichen Informationen" class="icon"><span class="text">Datenschutz</span></a>
                                <a href="/barrierefreiheit" title="Er&auml;uterung zur Barrierefreiheit" class="icon"><span class="text">Barrierefreiheit</span></a>
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    </body>
    <script src="/decorations/layout/ingrid/scripts/all.js"></script>
    <script src="/decorations/layout/ingrid/scripts/popup.js"></script>
</html>
