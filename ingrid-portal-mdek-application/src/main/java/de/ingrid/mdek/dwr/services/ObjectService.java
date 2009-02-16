package de.ingrid.mdek.dwr.services;

import java.util.List;
import java.util.Map;

import de.ingrid.mdek.MdekUtils.WorkState;
import de.ingrid.mdek.MdekUtils.IdcEntityOrderBy;
import de.ingrid.mdek.MdekUtils.IdcQAEntitiesSelectionType;
import de.ingrid.mdek.MdekUtils.IdcWorkEntitiesSelectionType;
import de.ingrid.mdek.beans.object.MdekDataBean;
import de.ingrid.mdek.beans.query.ObjectSearchResultBean;
import de.ingrid.mdek.beans.query.ObjectStatisticsResultBean;
import de.ingrid.mdek.beans.query.ThesaurusStatisticsResultBean;

public interface ObjectService {

	public MdekDataBean getNodeData(String nodeUuid, String nodeType, Boolean useWorkingCopy);
	public MdekDataBean getPublishedNodeData(String nodeUuid);
	public MdekDataBean saveNodeData(MdekDataBean data, Boolean useWorkingCopy, Boolean forcePublicationCondition);
	// Change the title to 'newTitle' for the object with the given 'uuid'
	public void updateObjectTitle(String uuid, String newTitle);
	public MdekDataBean assignObjectToQA(MdekDataBean data);
	public MdekDataBean reassignObjectToAuthor(MdekDataBean data);
	public ObjectSearchResultBean getWorkObjects(IdcWorkEntitiesSelectionType selectionType, IdcEntityOrderBy orderBy, boolean orderAsc, Integer startHit, Integer numHits);
	public ObjectSearchResultBean getQAObjects(WorkState workState, IdcQAEntitiesSelectionType selectionType, IdcEntityOrderBy orderBy, boolean orderAsc, Integer startHit, Integer numHits);
	public Map<String, Object> copyNode(String nodeUuid, String dstNodeUuid, Boolean includeChildren);
	public void moveNode(String nodeUuid, String oldParentUuid, String newParentUuString, boolean forcePublicationCondition);
	public void deleteNode(String nodeUuid, Boolean forceDeleteReferences, Boolean markOnly);
	public MdekDataBean deleteObjectWorkingCopy(String nodeUuid, Boolean forceDeleteReferences, Boolean markOnly);
	public MdekDataBean createNewNode(String parentUuid);
	public List<String> getPathToObject(String targetUuid);
	public boolean canCutObject(String parentUuid);
	public boolean canCopyObject(String parentUuid);
	public ObjectStatisticsResultBean getObjectStatistics(String objUuid);
	public ThesaurusStatisticsResultBean getObjectThesaurusStatistics(String objUuid, boolean thesaurusTerms, int startHit, int numHits);
}