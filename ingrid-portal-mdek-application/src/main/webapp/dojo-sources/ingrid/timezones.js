/*-
 * **************************************************-
 * InGrid Portal MDEK Application
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
define(["dojo/_base/declare"], function (declare) {
    return declare(null, {
        getAll: function () {
            return [
                {"value": "Pacific/Niue (GMT-11:00)", "id": "(GMT-11:00) Niue"},
                {"value": "Pacific/Pago_Pago (GMT-11:00)", "id": "(GMT-11:00) Pago Pago"},
                {"value": "Pacific/Honolulu (GMT-10:00)", "id": "(GMT-10:00) Hawaii Time"},
                {"value": "Pacific/Rarotonga (GMT-10:00)", "id": "(GMT-10:00) Rarotonga"},
                {"value": "Pacific/Tahiti (GMT-10:00)", "id": "(GMT-10:00) Tahiti"},
                {"value": "Pacific/Marquesas (GMT-09:30)", "id": "(GMT-09:30) Marquesas"},
                {"value": "America/Anchorage (GMT-09:00)", "id": "(GMT-09:00) Alaska Time"},
                {"value": "Pacific/Gambier (GMT-09:00)", "id": "(GMT-09:00) Gambier"},
                {"value": "America/Los_Angeles (GMT-08:00)", "id": "(GMT-08:00) Pacific Time"},
                {"value": "America/Tijuana (GMT-08:00)", "id": "(GMT-08:00) Pacific Time - Tijuana"},
                {"value": "America/Vancouver (GMT-08:00)", "id": "(GMT-08:00) Pacific Time - Vancouver"},
                {"value": "America/Whitehorse (GMT-08:00)", "id": "(GMT-08:00) Pacific Time - Whitehorse"},
                {"value": "Pacific/Pitcairn (GMT-08:00)", "id": "(GMT-08:00) Pitcairn"},
                {"value": "America/Dawson_Creek (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Dawson Creek"},
                {"value": "America/Denver (GMT-07:00)", "id": "(GMT-07:00) Mountain Time"},
                {"value": "America/Edmonton (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Edmonton"},
                {"value": "America/Hermosillo (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Hermosillo"},
                {"value": "America/Mazatlan (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Chihuahua, Mazatlan"},
                {"value": "America/Phoenix (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Arizona"},
                {"value": "America/Yellowknife (GMT-07:00)", "id": "(GMT-07:00) Mountain Time - Yellowknife"},
                {"value": "America/Belize (GMT-06:00)", "id": "(GMT-06:00) Belize"},
                {"value": "America/Chicago (GMT-06:00)", "id": "(GMT-06:00) Central Time"},
                {"value": "America/Costa_Rica (GMT-06:00)", "id": "(GMT-06:00) Costa Rica"},
                {"value": "America/El_Salvador (GMT-06:00)", "id": "(GMT-06:00) El Salvador"},
                {"value": "America/Guatemala (GMT-06:00)", "id": "(GMT-06:00) Guatemala"},
                {"value": "America/Managua (GMT-06:00)", "id": "(GMT-06:00) Managua"},
                {"value": "America/Mexico_City (GMT-06:00)", "id": "(GMT-06:00) Central Time - Mexico City"},
                {"value": "America/Regina (GMT-06:00)", "id": "(GMT-06:00) Central Time - Regina"},
                {"value": "America/Tegucigalpa (GMT-06:00)", "id": "(GMT-06:00) Central Time - Tegucigalpa"},
                {"value": "America/Winnipeg (GMT-06:00)", "id": "(GMT-06:00) Central Time - Winnipeg"},
                {"value": "Pacific/Galapagos (GMT-06:00)", "id": "(GMT-06:00) Galapagos"},
                {"value": "America/Bogota (GMT-05:00)", "id": "(GMT-05:00) Bogota"},
                {"value": "America/Cancun (GMT-05:00)", "id": "(GMT-05:00) America Cancun"},
                {"value": "America/Cayman (GMT-05:00)", "id": "(GMT-05:00) Cayman"},
                {"value": "America/Guayaquil (GMT-05:00)", "id": "(GMT-05:00) Guayaquil"},
                {"value": "America/Havana (GMT-05:00)", "id": "(GMT-05:00) Havana"},
                {"value": "America/Iqaluit (GMT-05:00)", "id": "(GMT-05:00) Eastern Time - Iqaluit"},
                {"value": "America/Jamaica (GMT-05:00)", "id": "(GMT-05:00) Jamaica"},
                {"value": "America/Lima (GMT-05:00)", "id": "(GMT-05:00) Lima"},
                {"value": "America/Nassau (GMT-05:00)", "id": "(GMT-05:00) Nassau"},
                {"value": "America/New_York (GMT-05:00)", "id": "(GMT-05:00) Eastern Time"},
                {"value": "America/Panama (GMT-05:00)", "id": "(GMT-05:00) Panama"},
                {"value": "America/Port-au-Prince (GMT-05:00)", "id": "(GMT-05:00) Port-au-Prince"},
                {"value": "America/Rio_Branco (GMT-05:00)", "id": "(GMT-05:00) Rio Branco"},
                {"value": "America/Toronto (GMT-05:00)", "id": "(GMT-05:00) Eastern Time - Toronto"},
                {"value": "Pacific/Easter (GMT-05:00)", "id": "(GMT-05:00) Easter Island"},
                {"value": "America/Caracas (GMT-04:30)", "id": "(GMT-04:30) Caracas"},
                {"value": "America/Asuncion (GMT-03:00)", "id": "(GMT-03:00) Asuncion"},
                {"value": "America/Barbados (GMT-04:00)", "id": "(GMT-04:00) Barbados"},
                {"value": "America/Boa_Vista (GMT-04:00)", "id": "(GMT-04:00) Boa Vista"},
                {"value": "America/Campo_Grande (GMT-03:00)", "id": "(GMT-03:00) Campo Grande"},
                {"value": "America/Cuiaba (GMT-03:00)", "id": "(GMT-03:00) Cuiaba"},
                {"value": "America/Curacao (GMT-04:00)", "id": "(GMT-04:00) Curacao"},
                {"value": "America/Grand_Turk (GMT-04:00)", "id": "(GMT-04:00) Grand Turk"},
                {"value": "America/Guyana (GMT-04:00)", "id": "(GMT-04:00) Guyana"},
                {"value": "America/Halifax (GMT-04:00)", "id": "(GMT-04:00) Atlantic Time - Halifax"},
                {"value": "America/La_Paz (GMT-04:00)", "id": "(GMT-04:00) La Paz"},
                {"value": "America/Manaus (GMT-04:00)", "id": "(GMT-04:00) Manaus"},
                {"value": "America/Martinique (GMT-04:00)", "id": "(GMT-04:00) Martinique"},
                {"value": "America/Port_of_Spain (GMT-04:00)", "id": "(GMT-04:00) Port of Spain"},
                {"value": "America/Porto_Velho (GMT-04:00)", "id": "(GMT-04:00) Porto Velho"},
                {"value": "America/Puerto_Rico (GMT-04:00)", "id": "(GMT-04:00) Puerto Rico"},
                {"value": "America/Santo_Domingo (GMT-04:00)", "id": "(GMT-04:00) Santo Domingo"},
                {"value": "America/Thule (GMT-04:00)", "id": "(GMT-04:00) Thule"},
                {"value": "Atlantic/Bermuda (GMT-04:00)", "id": "(GMT-04:00) Bermuda"},
                {"value": "America/St_Johns (GMT-03:30)", "id": "(GMT-03:30) Newfoundland Time - St. Johns"},
                {"value": "America/Araguaina (GMT-03:00)", "id": "(GMT-03:00) Araguaina"},
                {"value": "America/Argentina/Buenos_Aires (GMT-03:00)", "id": "(GMT-03:00) Buenos Aires"},
                {"value": "America/Bahia (GMT-03:00)", "id": "(GMT-03:00) Salvador"},
                {"value": "America/Belem (GMT-03:00)", "id": "(GMT-03:00) Belem"},
                {"value": "America/Cayenne (GMT-03:00)", "id": "(GMT-03:00) Cayenne"},
                {"value": "America/Fortaleza (GMT-03:00)", "id": "(GMT-03:00) Fortaleza"},
                {"value": "America/Godthab (GMT-03:00)", "id": "(GMT-03:00) Godthab"},
                {"value": "America/Maceio (GMT-03:00)", "id": "(GMT-03:00) Maceio"},
                {"value": "America/Miquelon (GMT-03:00)", "id": "(GMT-03:00) Miquelon"},
                {"value": "America/Montevideo (GMT-03:00)", "id": "(GMT-03:00) Montevideo"},
                {"value": "America/Paramaribo (GMT-03:00)", "id": "(GMT-03:00) Paramaribo"},
                {"value": "America/Recife (GMT-03:00)", "id": "(GMT-03:00) Recife"},
                {"value": "America/Santiago (GMT-03:00)", "id": "(GMT-03:00) Santiago"},
                {"value": "America/Sao_Paulo (GMT-02:00)", "id": "(GMT-02:00) Sao Paulo"},
                {"value": "Antarctica/Palmer (GMT-03:00)", "id": "(GMT-03:00) Palmer"},
                {"value": "Antarctica/Rothera (GMT-03:00)", "id": "(GMT-03:00) Rothera"},
                {"value": "Atlantic/Stanley (GMT-03:00)", "id": "(GMT-03:00) Stanley"},
                {"value": "America/Noronha (GMT-02:00)", "id": "(GMT-02:00) Noronha"},
                {"value": "Atlantic/South_Georgia (GMT-02:00)", "id": "(GMT-02:00) South Georgia"},
                {"value": "America/Scoresbysund (GMT-01:00)", "id": "(GMT-01:00) Scoresbysund"},
                {"value": "Atlantic/Azores (GMT-01:00)", "id": "(GMT-01:00) Azores"},
                {"value": "Atlantic/Cape_Verde (GMT-01:00)", "id": "(GMT-01:00) Cape Verde"},
                {"value": "Africa/Abidjan (GMT+00:00)", "id": "(GMT+00:00) Abidjan"},
                {"value": "Africa/Accra (GMT+00:00)", "id": "(GMT+00:00) Accra"},
                {"value": "Africa/Bissau (GMT+00:00)", "id": "(GMT+00:00) Bissau"},
                {"value": "Africa/Casablanca (GMT+00:00)", "id": "(GMT+00:00) Casablanca"},
                {"value": "Africa/El_Aaiun (GMT+00:00)", "id": "(GMT+00:00) El Aaiun"},
                {"value": "Africa/Monrovia (GMT+00:00)", "id": "(GMT+00:00) Monrovia"},
                {"value": "America/Danmarkshavn (GMT+00:00)", "id": "(GMT+00:00) Danmarkshavn"},
                {"value": "Atlantic/Canary (GMT+00:00)", "id": "(GMT+00:00) Canary Islands"},
                {"value": "Atlantic/Faroe (GMT+00:00)", "id": "(GMT+00:00) Faeroe"},
                {"value": "Atlantic/Reykjavik (GMT+00:00)", "id": "(GMT+00:00) Reykjavik"},
                {"value": "Etc/GMT (GMT+00:00)", "id": "(GMT+00:00) GMT (no daylight saving)"},
                {"value": "Europe/Dublin (GMT+00:00)", "id": "(GMT+00:00) Dublin"},
                {"value": "Europe/Lisbon (GMT+00:00)", "id": "(GMT+00:00) Lisbon"},
                {"value": "Europe/London (GMT+00:00)", "id": "(GMT+00:00) London"},
                {"value": "Africa/Algiers (GMT+01:00)", "id": "(GMT+01:00) Algiers"},
                {"value": "Africa/Ceuta (GMT+01:00)", "id": "(GMT+01:00) Ceuta"},
                {"value": "Africa/Lagos (GMT+01:00)", "id": "(GMT+01:00) Lagos"},
                {"value": "Africa/Ndjamena (GMT+01:00)", "id": "(GMT+01:00) Ndjamena"},
                {"value": "Africa/Tunis (GMT+01:00)", "id": "(GMT+01:00) Tunis"},
                {"value": "Africa/Windhoek (GMT+02:00)", "id": "(GMT+02:00) Windhoek"},
                {"value": "Europe/Amsterdam (GMT+01:00)", "id": "(GMT+01:00) Amsterdam"},
                {"value": "Europe/Andorra (GMT+01:00)", "id": "(GMT+01:00) Andorra"},
                {"value": "Europe/Belgrade (GMT+01:00)", "id": "(GMT+01:00) Central European Time - Belgrade"},
                {"value": "Europe/Berlin (GMT+01:00)", "id": "(GMT+01:00) Berlin"},
                {"value": "Europe/Brussels (GMT+01:00)", "id": "(GMT+01:00) Brussels"},
                {"value": "Europe/Budapest (GMT+01:00)", "id": "(GMT+01:00) Budapest"},
                {"value": "Europe/Copenhagen (GMT+01:00)", "id": "(GMT+01:00) Copenhagen"},
                {"value": "Europe/Gibraltar (GMT+01:00)", "id": "(GMT+01:00) Gibraltar"},
                {"value": "Europe/Luxembourg (GMT+01:00)", "id": "(GMT+01:00) Luxembourg"},
                {"value": "Europe/Madrid (GMT+01:00)", "id": "(GMT+01:00) Madrid"},
                {"value": "Europe/Malta (GMT+01:00)", "id": "(GMT+01:00) Malta"},
                {"value": "Europe/Monaco (GMT+01:00)", "id": "(GMT+01:00) Monaco"},
                {"value": "Europe/Oslo (GMT+01:00)", "id": "(GMT+01:00) Oslo"},
                {"value": "Europe/Paris (GMT+01:00)", "id": "(GMT+01:00) Paris"},
                {"value": "Europe/Prague (GMT+01:00)", "id": "(GMT+01:00) Central European Time - Prague"},
                {"value": "Europe/Rome (GMT+01:00)", "id": "(GMT+01:00) Rome"},
                {"value": "Europe/Stockholm (GMT+01:00)", "id": "(GMT+01:00) Stockholm"},
                {"value": "Europe/Tirane (GMT+01:00)", "id": "(GMT+01:00) Tirane"},
                {"value": "Europe/Vienna (GMT+01:00)", "id": "(GMT+01:00) Vienna"},
                {"value": "Europe/Warsaw (GMT+01:00)", "id": "(GMT+01:00) Warsaw"},
                {"value": "Europe/Zurich (GMT+01:00)", "id": "(GMT+01:00) Zurich"},
                {"value": "Africa/Cairo (GMT+02:00)", "id": "(GMT+02:00) Cairo"},
                {"value": "Africa/Johannesburg (GMT+02:00)", "id": "(GMT+02:00) Johannesburg"},
                {"value": "Africa/Maputo (GMT+02:00)", "id": "(GMT+02:00) Maputo"},
                {"value": "Africa/Tripoli (GMT+02:00)", "id": "(GMT+02:00) Tripoli"},
                {"value": "Asia/Amman (GMT+02:00)", "id": "(GMT+02:00) Amman"},
                {"value": "Asia/Beirut (GMT+02:00)", "id": "(GMT+02:00) Beirut"},
                {"value": "Asia/Damascus (GMT+02:00)", "id": "(GMT+02:00) Damascus"},
                {"value": "Asia/Gaza (GMT+02:00)", "id": "(GMT+02:00) Gaza"},
                {"value": "Asia/Jerusalem (GMT+02:00)", "id": "(GMT+02:00) Jerusalem"},
                {"value": "Asia/Nicosia (GMT+02:00)", "id": "(GMT+02:00) Nicosia"},
                {"value": "Europe/Athens (GMT+02:00)", "id": "(GMT+02:00) Athens"},
                {"value": "Europe/Bucharest (GMT+02:00)", "id": "(GMT+02:00) Bucharest"},
                {"value": "Europe/Chisinau (GMT+02:00)", "id": "(GMT+02:00) Chisinau"},
                {"value": "Europe/Helsinki (GMT+02:00)", "id": "(GMT+02:00) Helsinki"},
                {"value": "Europe/Istanbul (GMT+02:00)", "id": "(GMT+02:00) Istanbul"},
                {"value": "Europe/Kaliningrad (GMT+02:00)", "id": "(GMT+02:00) Moscow-01 - Kaliningrad"},
                {"value": "Europe/Kiev (GMT+02:00)", "id": "(GMT+02:00) Kiev"},
                {"value": "Europe/Riga (GMT+02:00)", "id": "(GMT+02:00) Riga"},
                {"value": "Europe/Sofia (GMT+02:00)", "id": "(GMT+02:00) Sofia"},
                {"value": "Europe/Tallinn (GMT+02:00)", "id": "(GMT+02:00) Tallinn"},
                {"value": "Europe/Vilnius (GMT+02:00)", "id": "(GMT+02:00) Vilnius"},
                {"value": "Africa/Khartoum (GMT+03:00)", "id": "(GMT+03:00) Khartoum"},
                {"value": "Africa/Nairobi (GMT+03:00)", "id": "(GMT+03:00) Nairobi"},
                {"value": "Antarctica/Syowa (GMT+03:00)", "id": "(GMT+03:00) Syowa"},
                {"value": "Asia/Baghdad (GMT+03:00)", "id": "(GMT+03:00) Baghdad"},
                {"value": "Asia/Qatar (GMT+03:00)", "id": "(GMT+03:00) Qatar"},
                {"value": "Asia/Riyadh (GMT+03:00)", "id": "(GMT+03:00) Riyadh"},
                {"value": "Europe/Minsk (GMT+03:00)", "id": "(GMT+03:00) Minsk"},
                {"value": "Europe/Moscow (GMT+03:00)", "id": "(GMT+03:00) Moscow+00 - Moscow"},
                {"value": "Asia/Tehran (GMT+03:30)", "id": "(GMT+03:30) Tehran"},
                {"value": "Asia/Baku (GMT+04:00)", "id": "(GMT+04:00) Baku"},
                {"value": "Asia/Dubai (GMT+04:00)", "id": "(GMT+04:00) Dubai"},
                {"value": "Asia/Tbilisi (GMT+04:00)", "id": "(GMT+04:00) Tbilisi"},
                {"value": "Asia/Yerevan (GMT+04:00)", "id": "(GMT+04:00) Yerevan"},
                {"value": "Europe/Samara (GMT+04:00)", "id": "(GMT+04:00) Moscow+01 - Samara"},
                {"value": "Indian/Mahe (GMT+04:00)", "id": "(GMT+04:00) Mahe"},
                {"value": "Indian/Mauritius (GMT+04:00)", "id": "(GMT+04:00) Mauritius"},
                {"value": "Indian/Reunion (GMT+04:00)", "id": "(GMT+04:00) Reunion"},
                {"value": "Asia/Kabul (GMT+04:30)", "id": "(GMT+04:30) Kabul"},
                {"value": "Antarctica/Mawson (GMT+05:00)", "id": "(GMT+05:00) Mawson"},
                {"value": "Asia/Aqtau (GMT+05:00)", "id": "(GMT+05:00) Aqtau"},
                {"value": "Asia/Aqtobe (GMT+05:00)", "id": "(GMT+05:00) Aqtobe"},
                {"value": "Asia/Ashgabat (GMT+05:00)", "id": "(GMT+05:00) Ashgabat"},
                {"value": "Asia/Dushanbe (GMT+05:00)", "id": "(GMT+05:00) Dushanbe"},
                {"value": "Asia/Karachi (GMT+05:00)", "id": "(GMT+05:00) Karachi"},
                {"value": "Asia/Tashkent (GMT+05:00)", "id": "(GMT+05:00) Tashkent"},
                {"value": "Asia/Yekaterinburg (GMT+05:00)", "id": "(GMT+05:00) Moscow+02 - Yekaterinburg"},
                {"value": "Indian/Kerguelen (GMT+05:00)", "id": "(GMT+05:00) Kerguelen"},
                {"value": "Indian/Maldives (GMT+05:00)", "id": "(GMT+05:00) Maldives"},
                {"value": "Asia/Calcutta (GMT+05:30)", "id": "(GMT+05:30) India Standard Time"},
                {"value": "Asia/Colombo (GMT+05:30)", "id": "(GMT+05:30) Colombo"},
                {"value": "Asia/Katmandu (GMT+05:45)", "id": "(GMT+05:45) Katmandu"},
                {"value": "Antarctica/Vostok (GMT+06:00)", "id": "(GMT+06:00) Vostok"},
                {"value": "Asia/Almaty (GMT+06:00)", "id": "(GMT+06:00) Almaty"},
                {"value": "Asia/Bishkek (GMT+06:00)", "id": "(GMT+06:00) Bishkek"},
                {"value": "Asia/Dhaka (GMT+06:00)", "id": "(GMT+06:00) Dhaka"},
                {"value": "Asia/Omsk (GMT+06:00)", "id": "(GMT+06:00) Moscow+03 - Omsk, Novosibirsk"},
                {"value": "Asia/Thimphu (GMT+06:00)", "id": "(GMT+06:00) Thimphu"},
                {"value": "Indian/Chagos (GMT+06:00)", "id": "(GMT+06:00) Chagos"},
                {"value": "Asia/Rangoon (GMT+06:30)", "id": "(GMT+06:30) Rangoon"},
                {"value": "Indian/Cocos (GMT+06:30)", "id": "(GMT+06:30) Cocos"},
                {"value": "Antarctica/Davis (GMT+07:00)", "id": "(GMT+07:00) Davis"},
                {"value": "Asia/Bangkok (GMT+07:00)", "id": "(GMT+07:00) Bangkok"},
                {"value": "Asia/Hovd (GMT+07:00)", "id": "(GMT+07:00) Hovd"},
                {"value": "Asia/Jakarta (GMT+07:00)", "id": "(GMT+07:00) Jakarta"},
                {"value": "Asia/Krasnoyarsk (GMT+07:00)", "id": "(GMT+07:00) Moscow+04 - Krasnoyarsk"},
                {"value": "Asia/Saigon (GMT+07:00)", "id": "(GMT+07:00) Hanoi"},
                {"value": "Asia/Ho_Chi_Minh (GMT+07:00)", "id": "(GMT+07:00) Ho Chi Minh"},
                {"value": "Indian/Christmas (GMT+07:00)", "id": "(GMT+07:00) Christmas"},
                {"value": "Antarctica/Casey (GMT+08:00)", "id": "(GMT+08:00) Casey"},
                {"value": "Asia/Brunei (GMT+08:00)", "id": "(GMT+08:00) Brunei"},
                {"value": "Asia/Choibalsan (GMT+08:00)", "id": "(GMT+08:00) Choibalsan"},
                {"value": "Asia/Hong_Kong (GMT+08:00)", "id": "(GMT+08:00) Hong Kong"},
                {"value": "Asia/Irkutsk (GMT+08:00)", "id": "(GMT+08:00) Moscow+05 - Irkutsk"},
                {"value": "Asia/Kuala_Lumpur (GMT+08:00)", "id": "(GMT+08:00) Kuala Lumpur"},
                {"value": "Asia/Macau (GMT+08:00)", "id": "(GMT+08:00) Macau"},
                {"value": "Asia/Makassar (GMT+08:00)", "id": "(GMT+08:00) Makassar"},
                {"value": "Asia/Manila (GMT+08:00)", "id": "(GMT+08:00) Manila"},
                {"value": "Asia/Shanghai (GMT+08:00)", "id": "(GMT+08:00) China Time - Beijing"},
                {"value": "Asia/Singapore (GMT+08:00)", "id": "(GMT+08:00) Singapore"},
                {"value": "Asia/Taipei (GMT+08:00)", "id": "(GMT+08:00) Taipei"},
                {"value": "Asia/Ulaanbaatar (GMT+08:00)", "id": "(GMT+08:00) Ulaanbaatar"},
                {"value": "Australia/Perth (GMT+08:00)", "id": "(GMT+08:00) Western Time - Perth"},
                {"value": "Asia/Pyongyang (GMT+08:30)", "id": "(GMT+08:30) Pyongyang"},
                {"value": "Asia/Dili (GMT+09:00)", "id": "(GMT+09:00) Dili"},
                {"value": "Asia/Jayapura (GMT+09:00)", "id": "(GMT+09:00) Jayapura"},
                {"value": "Asia/Seoul (GMT+09:00)", "id": "(GMT+09:00) Seoul"},
                {"value": "Asia/Tokyo (GMT+09:00)", "id": "(GMT+09:00) Tokyo"},
                {"value": "Asia/Yakutsk (GMT+09:00)", "id": "(GMT+09:00) Moscow+06 - Yakutsk"},
                {"value": "Pacific/Palau (GMT+09:00)", "id": "(GMT+09:00) Palau"},
                {"value": "Australia/Adelaide (GMT+10:30)", "id": "(GMT+10:30) Central Time - Adelaide"},
                {"value": "Australia/Darwin (GMT+09:30)", "id": "(GMT+09:30) Central Time - Darwin"},
                {"value": "Antarctica/DumontDUrville (GMT+10:00)", "id": "(GMT+10:00) Dumont D'Urville"},
                {"value": "Asia/Magadan (GMT+10:00)", "id": "(GMT+10:00) Moscow+07 - Magadan"},
                {"value": "Asia/Vladivostok (GMT+10:00)", "id": "(GMT+10:00) Moscow+07 - Yuzhno-Sakhalinsk"},
                {"value": "Australia/Brisbane (GMT+10:00)", "id": "(GMT+10:00) Eastern Time - Brisbane"},
                {"value": "Australia/Hobart (GMT+11:00)", "id": "(GMT+11:00) Eastern Time - Hobart"},
                {"value": "Australia/Sydney (GMT+11:00)", "id": "(GMT+11:00) Eastern Time - Melbourne, Sydney"},
                {"value": "Pacific/Chuuk (GMT+10:00)", "id": "(GMT+10:00) Truk"},
                {"value": "Pacific/Guam (GMT+10:00)", "id": "(GMT+10:00) Guam"},
                {"value": "Pacific/Port_Moresby (GMT+10:00)", "id": "(GMT+10:00) Port Moresby"},
                {"value": "Pacific/Efate (GMT+11:00)", "id": "(GMT+11:00) Efate"},
                {"value": "Pacific/Guadalcanal (GMT+11:00)", "id": "(GMT+11:00) Guadalcanal"},
                {"value": "Pacific/Kosrae (GMT+11:00)", "id": "(GMT+11:00) Kosrae"},
                {"value": "Pacific/Norfolk (GMT+11:00)", "id": "(GMT+11:00) Norfolk"},
                {"value": "Pacific/Noumea (GMT+11:00)", "id": "(GMT+11:00) Noumea"},
                {"value": "Pacific/Pohnpei (GMT+11:00)", "id": "(GMT+11:00) Ponape"},
                {"value": "Asia/Kamchatka (GMT+12:00)", "id": "(GMT+12:00) Moscow+09 - Petropavlovsk-Kamchatskiy"},
                {"value": "Pacific/Auckland (GMT+13:00)", "id": "(GMT+13:00) Auckland"},
                {"value": "Pacific/Fiji (GMT+13:00)", "id": "(GMT+13:00) Fiji"},
                {"value": "Pacific/Funafuti (GMT+12:00)", "id": "(GMT+12:00) Funafuti"},
                {"value": "Pacific/Kwajalein (GMT+12:00)", "id": "(GMT+12:00) Kwajalein"},
                {"value": "Pacific/Majuro (GMT+12:00)", "id": "(GMT+12:00) Majuro"},
                {"value": "Pacific/Nauru (GMT+12:00)", "id": "(GMT+12:00) Nauru"},
                {"value": "Pacific/Tarawa (GMT+12:00)", "id": "(GMT+12:00) Tarawa"},
                {"value": "Pacific/Wake (GMT+12:00)", "id": "(GMT+12:00) Wake"},
                {"value": "Pacific/Wallis (GMT+12:00)", "id": "(GMT+12:00) Wallis"},
                {"value": "Pacific/Apia (GMT+14:00)", "id": "(GMT+14:00) Apia"},
                {"value": "Pacific/Enderbury (GMT+13:00)", "id": "(GMT+13:00) Enderbury"},
                {"value": "Pacific/Fakaofo (GMT+13:00)", "id": "(GMT+13:00) Fakaofo"},
                {"value": "Pacific/Tongatapu (GMT+13:00)", "id": "(GMT+13:00) Tongatapu"},
                {"value": "Pacific/Kiritimati (GMT+14:00)", "id": "(GMT+14:00) Kiritimati"}
            ]
        }
    })();
});
