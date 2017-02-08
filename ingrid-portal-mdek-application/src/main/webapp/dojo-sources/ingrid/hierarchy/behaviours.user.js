/*
 * **************************************************-
 * InGrid Portal MDEK Application
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    "dojo/aspect",
    "dojo/Deferred",
    "dojo/dom",
    "dojo/dom-construct",
    "dojo/dom-class",
    "dojo/dom-style",
    "dojo/query",
    "dojo/topic",
    "dijit/registry",
    "dijit/form/Button",
    "ingrid/IgeEvents",
    "ingrid/layoutCreator",
    "ingrid/grid/CustomGridEditors",
    "ingrid/grid/CustomGridFormatters",
    "ingrid/hierarchy/behaviours",
    "ingrid/utils/Catalog",
    "ingrid/utils/Syslist",
    "ingrid/widgets/UvpPhases",
    "ingrid/widgets/NominatimSearch"
], function(array, lang, aspect, Deferred, dom, construct, domClass, domStyle, query, topic, registry, Button, IgeEvents, creator, Editors, Formatters, behaviours, Catalog, UtilSyslist, UvpPhases, NominatimSearch) {

    return lang.mixin(behaviours, {

        uvpInitialFolders: {
            title: "UVP: Initiale Ordner",
            description: "Anlegen der initialen Ordner, falls diese noch nicht bereits vorhanden sind.",
            defaultActive: true,
            type: "SYSTEM",
            initFlagName: "uvp_initialized",
            run: function() {
                var self = this;
                this.getInitFlag().then(function(isInitialized) {
                    console.log("init flag: ", isInitialized);
                    if (!isInitialized) {
                        self.createFolders();
                        self.storeInitFlag();
                    }
                });
            },

            createFolders: function() {
                var def = new Deferred();
                var def2 = new Deferred();
                var self = this;
                ObjectService.createNewNode(null, function(objNode) {
                    objNode.nodeAppType = "O";
                    objNode.objectClass = "1000";
                    objNode.objectName = "UVPs";
                    ObjectService.saveNodeData(objNode, "true", false, {
                        callback: def.resolve,
                        errorHandler: self.handleCreateError
                    });
                });
                ObjectService.createNewNode(null, function(objNode) {
                    objNode.nodeAppType = "O";
                    objNode.objectClass = "1000";
                    objNode.objectName = "negative UVPs";
                    def.then(function() {
                        ObjectService.saveNodeData(objNode, "true", false, {
                            callback: def2.resolve,
                            errorHandler: self.handleCreateError
                        });
                    })
                });
                ObjectService.createNewNode(null, function(objNode) {
                    objNode.nodeAppType = "O";
                    objNode.objectClass = "1000";
                    objNode.objectName = "Ausländische UVPs";
                    def2.then(function() {
                        ObjectService.saveNodeData(objNode, "true", false, {
                            errorHandler: self.handleCreateError
                        });
                    });
                });
            },

            handleCreateError: function(error) {
                console.error("Error during initial folder creation for UVP:", error);
            },

            getInitFlag: function() {
                var self = this;
                return Catalog.getGenericValuesDef([this.initFlagName])
                    .then(function(values) { return values[self.initFlagName]; });
            },

            storeInitFlag: function() {
                var obj = {};
                obj[this.initFlagName] = true;
                Catalog.storeGenericValuesDef(obj);
            }
        },

        uvpDocumentTypes: {
            title: "UVP Dokumenten Typen und Verhalten",
            description: "Definition der Dokumententypen: UVP, ...",
            defaultActive: true,
            type: "SYSTEM",
            run: function() {

                topic.subscribe("/afterInitDialog/ChooseWizard", function(data) {
                    // remove all assistants
                    data.assistants.splice(0, data.assistants.length);
                });

                // load custom syslists
                topic.subscribe("/collectAdditionalSyslistsToLoad", function(ids) {
                    ids.push(8001, 9000);
                });

                // get availbale object classes from codelist 8001
                UtilSyslist.listIdObjectClass = 8001;

                this.hideMenuItems();

                var self = this;
                topic.subscribe("/onPageInitialized", function(page) {
                    if (page === "Hiearchy") {
                        self.handleTreeOperations();
                    }
                });

            },

            handleTreeOperations: function() {
                topic.subscribe("/selectNode", function(message) {
                    if (message.id === "dataTree") {
                        // do not allow to add new objects directly under the root node
                        if (message.node.id === "objectRoot") {
                            console.log("disable create/paste new object");
                            registry.byId("toolbarBtnNewDoc").set("disabled", true);
                            registry.byId("toolbarBtnPaste").set("disabled", true);
                        } else if (message.node.parent === "objectRoot") {
                            // do not allow to rename or delete the folders directly under the root node
                            registry.byId("toolbarBtnCut").set("disabled", true);
                            registry.byId("toolbarBtnCopy").set("disabled", true);
                            registry.byId("toolbarBtnCopySubTree").set("disabled", true);
                            registry.byId("toolbarBtnPaste").set("disabled", true);
                            registry.byId("toolbarBtnSave").set("disabled", true);
                            registry.byId("toolbarBtnDelSubTree").set("disabled", true);
                        }
                    }
                });

                topic.subscribe("/onTreeContextMenu", function(node) {
                    console.log("context menu called from:", node);
                    if (node.item.id === "objectRoot") {
                        registry.byId("menuItemNew").set("disabled", true);
                        registry.byId("menuItemPaste").set("disabled", true);
                    } else if (node.item.parent === "objectRoot") {
                        registry.byId("menuItemPreview").set("disabled", true);
                        registry.byId("menuItemCut").set("disabled", true);
                        registry.byId("menuItemCopySingle").set("disabled", true);
                        registry.byId("menuItemCopy").set("disabled", true);
                        registry.byId("menuItemPaste").set("disabled", true);
                        registry.byId("menuItemDelete").set("disabled", true);
                        registry.byId("menuItemPublicationCondition1").set("disabled", true);
                        registry.byId("menuItemPublicationCondition2").set("disabled", true);
                        registry.byId("menuItemPublicationCondition3").set("disabled", true);
                    }
                });
            },

            hideMenuItems: function() {
                topic.subscribe("/onMenuBarCreate", function(excludedItems) {
                    excludedItems.push("menuPageStatistics", "menuPageQualityEditor", "menuPageQualityAssurance",
                        "menuPageResearchThesaurus");
                });
                // TODO: remove stack container or do not let them initialized
                // registry.byId("stackContainer").removeChild(registry.byId("pageStatistics"));
            }

        },

        uvpPhaseField: {
            title: "UVP Phasen Feld",
            description: "Hinzufügen von dynamischen Feldern",
            defaultActive: true,
            prefix: "uvp_",
            run: function() {

                // rename default fields
                query("#generalDescLabel label").addContent("Bekanntmachungstext", "only");
                query("#objectNameLabel label").addContent("Vorhabenbezeichnung", "only");
                query("#general .titleBar .titleCaption").addContent("Bekanntmachung", "only");
                query("#general .titleBar").attr("title", "Für die allgemeine Vorhabenbeschreibung sollte der Einfachheit halber der Text der Bekanntmachung verwendet werden.");
                dom.byId("generalAddressTableLabelText").innerHTML = "Federführende Behörde";

                // rename Objekte root node
                registry.byId("dataTree").rootNode.getChildren()[0].set("label", "Vorhaben");

                // do not override my address title
                IgeEvents.setGeneralAddressLabel = function() { };

                this.hideDefaultFields();

                this.createFields();

                // TODO: additional fields according to #490 and #473

                var self = this;
                topic.subscribe("/onObjectClassChange", function(clazz) {
                    self.prepareDocument(clazz);
                });
            },

            prepareDocument: function(classInfo) {
                console.log("Prepare document for class: ", classInfo);
                var objClass = classInfo.objClass;
                if (objClass === "Class10") {

                } else if (objClass === "Class11") {

                } else if (objClass === "Class12") {

                }
            },

            hideDefaultFields: function() {
                domStyle.set("widget_objectName", "width", "550px");

                domClass.add(dom.byId("objectClassLabel").parentNode, "hide");
                domClass.add(dom.byId("objectOwnerLabel").parentNode, "hide");

                domClass.add(registry.byId("toolbarBtnISO").domNode, "hide");

                domClass.add("uiElement5000", "hide");
                domClass.add("uiElement5100", "hide");
                domClass.add("uiElement5105", "hide");
                domClass.add("uiElement6010", "hide");

                // hide all rubrics
                query(".rubric", "contentFrameBodyObject").forEach(function(item) {
                    if (item.id !== "general") {
                        domClass.add(item, "hide");
                    }
                });
            },

            createFields: function() {
                var rubric = "general";

                new UvpPhases({ id: "UVPPhases" }).placeAt("generalContent");

                /**
                 * Vorhabensnummer
                 */
                var structure = [
                    {
                        field: 'categoryId',
                        name: 'Kategorie',
                        type: Editors.SelectboxEditor,
                        editable: true,
                        listId: 9000,
                        formatter: lang.partial(Formatters.SyslistCellFormatter, 9000),
                        partialSearch: true
                    }
                ];

                this.createSpatial(rubric);

                /**
                 * Category
                 */
                var id = "uvpgCategory";
                creator.createDomDataGrid(
                    { id: id, name: "Vorhabensnummer", help: "...", isMandatory: true, visible: "optional", rows: "4", forceGridHeight: false, style: "width:100%" },
                    structure, rubric
                );
                var categoryWidget = registry.byId(id);
                domClass.add(categoryWidget.domNode, "hideTableHeader");

                require("ingrid/IgeActions").additionalFieldWidgets.push(categoryWidget);

            },

            createSpatial: function(rubric) {
                // spatial reference
                creator.addToSection(rubric, creator.createDomTextbox({ id: this.prefix + "spatialValue", name: "Raumbezug", help: "...", isMandatory: true, visible: "optional", style: "width:100%" }));
                var spatialInput = registry.byId(this.prefix + "spatialValue");
                spatialInput.set("disabled", true);

                var self = this;
                var spatialViewButton = new Button({
                    id: this.prefix + "btnSpatialValueShow",
                    label: "Anzeigen",
                    disabled: true,
                    "class": "optional show right",
                    onClick: function() {
                        var val = spatialInput.get("value");
                        var fixedValue = val.indexOf(": ") === -1 ? val : val.substr(val.indexOf(": ") + 2);
                        var arrayValue = fixedValue.split(',');

                        self.nominatimSearch._zoomToBoundingBox([arrayValue[1], arrayValue[3], arrayValue[0], arrayValue[2]], true);
                    }
                }).placeAt(rubric);
                // layout fix!
                construct.place(construct.toDom("<div class='clear'></div>"), rubric);

                spatialInput.on("change", function(value) {
                    if (value === "") {
                        spatialViewButton.set("disabled", true);
                    } else {
                        spatialViewButton.set("disabled", false);
                    }
                });

                /**
                 * Map
                 */
                this.nominatimSearch = new NominatimSearch().placeAt(rubric);
                aspect.after(this.nominatimSearch, "onData", function(meta, args) {
                    var bbox = args[0];
                    var title = args[1];
                    console.log("Received bbox:", bbox);
                    if (title) bbox = title + ": " + bbox;
                    spatialInput.set("value", bbox);
                });

                require("ingrid/IgeActions").additionalFieldWidgets.push(spatialInput);
            }
        }

    });
});
