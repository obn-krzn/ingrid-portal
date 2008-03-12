(
  {
    "general.ok":"Ok",
    "general.cancel":"Cancel",
    "general.error":"Error",
    "general.warning":"Warning",
    "general.hint":"Hinweis",
    "general.help":"Help - General",
    "general.showAddress":"Show Address",
    "general.delete":"Delete",
    "general.closeWindow":"All unsaved changes will be discarded.",

    "init.loadError":"Error while loading initial values from the server. Please check your connection and try again.",
   
    "tree.nodeNew":"New",
    "tree.nodePreview":"Preview",
    "tree.nodeCut":"Cut Object / Address / Subtree",
    "tree.nodeCopySingle":"Copy Object / Address",
    "tree.nodeCopy":"Copy Subtree",
    "tree.nodePaste":"Paste",
    "tree.nodeMarkDeleted":"Mark deleted",
    "tree.nodeDelete":"Delete",
    "tree.confirmMarkDeleted":"Do you really want to mark the Node '%{0}' as deleted?",
    "tree.confirmDelete":"Do you really want to delete the Node '%{0}'?",
    "tree.assistant1":"getCapabilities-Assistant",
    "tree.assistant2":"General Assistant",
    "tree.noAssistant":"No Assistant",
    "tree.selectNodeHint":"Please select a parent node.",
    "tree.selectNodeCutHint":"Please select a node to cut.",
    "tree.loadError":"Error while loading data from the server. Please check your connection and try again.",
    "tree.nodeCanCutError":"Error while preparing the node for a cut operation.",
    "tree.nodeCanPublishHint":"Node can not be published. Please fill out all required fields.",
    "tree.nodeCanCopyError":"Error while preparing the node for a copy operation.",
    "tree.nodeCopyError":"Error while copying the selected node. Node copy operation failed in the backend.",
    "tree.nodeCreateError":"Error while creating a new node. Node could not be created in the database.",
    "tree.nodeCreateLocalError":"Error while creating a new node. The node was created but could not be attached to the tree.",
    "tree.nodeDeleteError":"Error while deleting the selected node. Node delete operation failed in the backend.",
    "tree.nodeDiscardError":"Error while discarding the selected node. Node state could not be reset to its published version.",
    "tree.nodeLoadError":"Error while loading the node from the server. The node was not found!",
    "tree.nodeMoveError":"Error while moving the cut node. Node move operation failed in the backend.",
    "tree.nodePublishError":"Error while publishing the current object. Node publish operation failed in the backend.",
    "tree.selectNodeDeleteHint":"Please select a node to delete.",
    "tree.selectNodeCopyHint":"Please select a valid node to copy.",
    "tree.selectNodePasteHint":"Please select a target node to paste.",
    "tree.nodePasteNoCutCopyHint":"Please select a node to copy or cut before pasting.",
    "tree.nodePasteInvalidHint":"Please select a valid target to paste.",
    "tree.saveNewNodeHint":"Please save the currently edited node before pasting.",
    "tree.newNodeName":"New Object",
    "tree.newAddressName":"New Address",
    "tree.reload":"Reload",

    "spatial.noResultsHint":"No results found.",
    "spatial.loadingHint":"Please wait, loading...",
    "spatial.connectionError":"An error occured while contacting the SNS Service.",

	"extraInfo.publicationCondition.internet":"Internet",
	"extraInfo.publicationCondition.intranet":"Intranet",
	"extraInfo.publicationCondition.internal":"Internal",
	"extraInfo.publicationCondition.notShared":"Not Shared",

    "sns.timeoutError":"A timeout error occured while contacting the SNS Service. Please refine your search query.",
    "sns.connectionError":"An error occured while contacting the SNS Service.",
    "sns.loadingTopTermsHint":"Please wait, loading the SNS Top Terms...",
    "sns.loadingHint":"Please wait, loading...",
    "sns.processingQueryHint":"Please wait, processing query...",
    "sns.noResultHint":"The specified Term was not found.",
    "sns.noDescriptorHint":"The specified Descriptor was not found.",
	"sns.noSimilarTermsHint":"The SNS query returned no results.",
    "sns.numberOfTerms":"Number of Terms:",
    "sns.freeTermAddHint":"The specified term could not be found in the SNS. Therefore it was added to the list of loose search terms.",
    "sns.freeTermAddTopTermHint":"'%{0}' is a Thesaurus-Topterm! It can't be added to the free terms list!",
    "sns.freeTermAddNodeLabelHint":"'%{0}' is a node label! It can't be added to the free terms list!",

	"links.selectNodeHint":"Please select a target node.",
	"links.fillRequiredFieldsHint":"Please fill out all required fields.",
	"links.noLinkToSelfHint":"It is not possible to create a link to the same object.",

	"dialog.addDescriptorsTitle":"Add Descriptors",
	"dialog.addDescriptorsMessage":"Add the following descriptors as search terms?",
	"dialog.saveChangesTitle":"Save changes",
	"dialog.object.saveChangesHint":"The current Object or its comments were changed. Do you want to save the changes?",
	"dialog.address.saveChangesHint":"The current Address or its comments were changed. Do you want to save the changes?",
	"dialog.discardPubExistTitle":"Discard all changes",
	"dialog.object.discardPubExistMessage":"Do you really want to discard all changes made to the object '%{0}' and reset to the last published version?",
	"dialog.address.discardPubExistMessage":"Do you really want to discard all changes made to the address '%{0}' and reset to the last published version?",
	"dialog.discardPubNotExistTitle":"Discard new MD-S",
	"dialog.object.discardPubNotExistMessage":"Do you really want to delete '%{0}' and all it's subobjects?",
	"dialog.address.discardPubNotExistMessage":"Do you really want to delete '%{0}' and all it's subaddresses?",
	"dialog.undoChangesTitle":"Discard current changes",
	"dialog.object.undoChangesMessage":"Do you want to discard all changes to the current object since the last save?",
	"dialog.address.undoChangesMessage":"Do you want to discard all changes to the current address since the last save?",
	"dialog.inputInvalidError":"There was an error validating input fields. Please check all entered data and try again.",
	"dialog.generalError":"An error occured: '%{0}'",
	"dialog.undefinedError":"An Error occured.",
	"dialog.commentTitle":"Comments for '%{0}'",

	"operation.error.object.parentNotPublishedError":"You have to publish the parent objects before publishing the selected object!",
	"operation.error.address.parentNotPublishedError":"You have to publish the parent addresses before publishing the selected addresses!",
	"operation.error.targetIsSubnodeOfSourceError":"The target node of a move operation must not be a child of the source node.",
	"operation.error.object.subTreeHasWorkingCopiesError":"One or more subobjects have to be saved before the operation can be carried out.",
	"operation.error.address.subTreeHasWorkingCopiesError":"One or more subaddresses have to be saved before the operation can be carried out.",
	"operation.error.parentHasSmallerPublicationConditionError":"It is not possible to publish this node because its parent has a lower publication condition.",
	"operation.hint.publicationConditionSaveHint":"The subtree contains objects with an incompatible publication condition. By saving this object all subobjects will be modified. Do you really want to continue?",
	"operation.hint.publicationConditionMoveHint":"The subtree contains objects with an incompatible publication condition. By moving this object all subobjects will be modified. Do you really want to continue?",

	"address.type.institution":"Institution",
	"address.type.unit":"Unit",
	"address.type.person":"Person",
	"address.type.custom":"Custom Address",

    "table.selectAll":"Select all rows",
    "table.deselectAll":"Clear selection",
    "table.confirmDeleteSelected":"Do you really want to delete all selected rows?",
    "table.deleteSelected":"Delete selected rows",
    "table.confirmDelete":"Do you really want to delete the row '%{0}'?",
    "table.rowDelete":"Delete row",

    "table.confirmDeleteSelectedObj":"Do you really want to delete all selected objects/addresses?",
    "table.deleteSelectedObj":"Delete selected objects/addresses",
    "table.confirmDeleteObj":"Do you really want to delete the object/address '%{0}'?",
    "table.rowDeleteObj":"Delete object/address",

    "table.confirmDeleteSelectedAddressReference":"Do you really want to delete all selected references?",
    "table.deleteSelectedAddressReference":"Delete selected address reference",
    "table.confirmDeleteAddressReference":"Do you really want to delete the references '%{0}'?",
    "table.rowDeleteAddressReference":"Delete address reference",

    "table.confirmDeleteSelectedReference":"Do you really want to delete all selected references?",
    "table.deleteSelectedReference":"Delete refences in selected object/address",
    "table.confirmDeleteReference":"Do you really want to delete the references in '%{0}'?",
    "table.rowDeleteReference":"Delete references in object/address",

    "validation.minmax":"The Value of field '%{0}' must be larger than the value of field '%{1}'."
  }
)