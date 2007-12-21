package de.ingrid.mdek.dwr.sns;

import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.slb.taxi.webservice.xtm.stubs.FieldsType;
import com.slb.taxi.webservice.xtm.stubs.SearchType;
import com.slb.taxi.webservice.xtm.stubs.TopicMapFragment;

import de.ingrid.iplug.sns.*;
import de.ingrid.iplug.sns.utils.Topic;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.PlugDescription;
import de.ingrid.utils.query.FieldQuery;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.IDataTypes;
import de.ingrid.utils.queryparser.ParseException;
import de.ingrid.utils.queryparser.QueryStringParser;

public class SNSService {

	private final static Logger log = Logger.getLogger(SNSService.class);	

	// TODO Read from properties file
	private static PlugDescription fPlugDescription;

    static {
        fPlugDescription = new PlugDescription();
        fPlugDescription.setProxyServiceURL("aPlugId");
        fPlugDescription.put("serviceUrl", "http://www.semantic-network.de/service-xtm-2.0/xtm/soap");
        fPlugDescription.put("username", "ingrid_test");
        fPlugDescription.put("password", "ingrid_test");
        fPlugDescription.put("language", "de");
        fPlugDescription.putInt("maxWordAnalyzing", 100);
	}

    private static String SNS_ROOT_TOPIC = "toplevel"; 
    private static String THESAURUS_LANGUAGE_FILTER = "de";
    private SNSController snsController;
    private SNSClient snsClient;
    
    public SNSService() throws Exception {

    	snsClient = new SNSClient(
        		(String) fPlugDescription.get("username"),
        		(String)fPlugDescription.get("password"),
        		(String)fPlugDescription.get("language"),
        		new URL((String) fPlugDescription.get("serviceUrl")));

        snsController = new SNSController(snsClient, "ags:");
    }

    public ArrayList<SNSTopic> getRootTopics() {
    	return getSubTopics(SNS_ROOT_TOPIC, 1, "down");
    }
    
    public ArrayList<SNSTopic> getSubTopics(String topicID, long depth, String direction) {
    	return getSubTopics(topicID, depth, direction, false, false);
    }


    public ArrayList<SNSTopic> getSubTopicsWithRoot(String topicID, long depth, String direction) {
    	return getSubTopics(topicID, depth, direction, true, true);
    }
    
    public ArrayList<SNSTopic> getSubTopics(String topicID, long depth, String direction, boolean includeSiblings, boolean includeRootNode) {
    	ArrayList<SNSTopic> resultList = new ArrayList<SNSTopic>(); 
    	
    	// Create the Query
    	IngridQuery query = null;
    	try {
    		query = QueryStringParser.parse(topicID);
    	} catch (ParseException e) {log.error(e);}

    	query.addField(new FieldQuery(true, false, "datatype", IDataTypes.SNS));
        query.addField(new FieldQuery(true, false, "lang", "de"));
        query.put("includeSiblings", includeSiblings);
        query.put("association", "narrowerTermAssoc");
        query.put("depth", depth);
        query.put("direction", direction);
        query.putInt(Topic.REQUEST_TYPE, Topic.TOPIC_HIERACHY);
        
        IngridHit[] hitsArray = getHierarchy(topicID, depth, direction, includeSiblings);

        // TODO Build correct tree structure
        // TODO Check Language
        for (int i = 0; i < hitsArray.length; i++) {
            Topic hit = (Topic) hitsArray[i];

            final List successors = hit.getSuccessors();
            
            // TODO The returned root structure is invalid (?)
            if (topicID == SNS_ROOT_TOPIC) {
            	resultList = buildTopicRootStructure(successors);
            }
            else {
            	if (includeRootNode)
            	{
                	ArrayList<Topic> topNode = new ArrayList<Topic>();
                	topNode.add(hit);
                	resultList = buildTopicStructure(topNode);            	            		
            	}
            	else {
            		resultList = buildTopicStructure(successors);
            	}
            }
        }
        return resultList;
    }


    private static ArrayList<SNSTopic> buildTopicStructure(List<Topic> topics) {
    	ArrayList<SNSTopic> result = new ArrayList<SNSTopic>(); 

    	for (Topic topic : topics) {
    		SNSTopic resultTopic = new SNSTopic(getTypeFromTopic(topic), topic.getTopicID(), topic.getTopicName());
    		List<Topic> succ = topic.getSuccessors();

    		if (succ != null && !succ.isEmpty())
    		{
        		ArrayList<SNSTopic> children = buildTopicStructure(succ); 
    			resultTopic.setChildren(children);

    			for (SNSTopic child : children) {
        			ArrayList<SNSTopic> parents = new ArrayList<SNSTopic>();
        			parents.add(resultTopic);
    				child.setParents(parents);
    			}
    		}
    		result.add(resultTopic);
    	}
    	return result;
    }

    private static ArrayList<SNSTopic> buildTopicRootStructure(List<Topic> topicList) {
    	ArrayList<SNSTopic> result = new ArrayList<SNSTopic>(); 

    	TreeSet<Topic> topics = new TreeSet<Topic>(new SNSTopicComparator());
    	topics.addAll(topicList);

    	for (Topic topic : topics) {
    		SNSTopic resultTopic = new SNSTopic(getTypeFromTopic(topic), topic.getTopicID(), topic.getTopicName());
    		result.add(resultTopic);
    	}
    	return result;
    }

    
    private static Type getTypeFromTopic(Topic t) {
    	String nodeType = t.getSummary();

		if (nodeType.indexOf("topTermType") != -1) 
			return Type.TOP_TERM;
		else if (nodeType.indexOf("nodeLabelType") != -1) 
			return Type.NODE_LABEL;
		else if (nodeType.indexOf("descriptorType") != -1) 
			return Type.DESCRIPTOR;
		else
			return Type.TOP_TERM;
    }
    
    
    public ArrayList<SNSTopic> findTopics(String q) {
    	ArrayList<SNSTopic> results = new ArrayList<SNSTopic>();

    	IngridQuery query = null;
    	try {
    		query = QueryStringParser.parse(q);
    	} catch (ParseException e) {log.error(e);}

    	query.addField(new FieldQuery(true, false, "datatype", IDataTypes.SNS));
//        query.addField(new FieldQuery(true, false, "lang", "de"));
        query.putInt(Topic.REQUEST_TYPE, Topic.TOPIC_FROM_TERM);
//        IngridHits hits = plug.search(query, 0, 600);
        IngridHits hits = null;
        IngridHit[] hitsArray = hits.getHits();

        if (hitsArray != null)
        {
        	for (int i = 0; i < hitsArray.length; ++i) {
        		Topic t = (Topic) hitsArray[i];
        		System.out.println("Found: "+t.getTopicID()+", "+t.getTopicName());
        		results.add(new SNSTopic(getTypeFromTopic(t), t.getTopicID(), t.getTopicName()));
        	}
        }
        return results;
    }


    public void testIncludeSiblings() {
        // getHierarchy?root=uba_thes_27118&user=ingrid_test&password=ingrid_test&depth=2&direction=up&includeSiblings=true
    	// false liefert drei Knoten
    	// true liefert 10 Knoten
    
        IngridHit[] hitsArray = getHierarchy("uba_thes_27118", 2, "up", true);

        for (IngridHit hit : hitsArray) {
        	printTopic((Topic) hit);
        	
        }
    }

    private IngridHit[] getHierarchy(String topicID, long depth, String direction, boolean includeSiblings) {
	    int[] totalSize = new int[1];
	    totalSize[0] = 0;
	    Topic[] snsResults = new Topic[0];
	    try {
	    	snsResults = snsController.getTopicHierachy(totalSize, "narrowerTermAssoc", depth, direction, includeSiblings, "de", topicID, false, "mdek");
	    }
	    catch (Exception e) {log.error(e);}
	
	    totalSize[0] = snsResults.length;
	    IngridHits hits = new IngridHits("mdek", totalSize[0], snsResults, false);

	    return hits.getHits();
    }

    public SNSTopic findTopic(String queryTerm) {
    	TopicMapFragment mapFragment = null;
    	try {
	    	mapFragment = snsClient.findTopics(queryTerm, "/thesa", SearchType.exact,
	            FieldsType.names, 0, THESAURUS_LANGUAGE_FILTER);
	    } catch (Exception e) {
	    	log.error(e);
	    }
	    
	    if (null != mapFragment) {
	    	com.slb.taxi.webservice.xtm.stubs.xtm.Topic[] topics = mapFragment.getTopicMap().getTopic();
	        if ((null != topics) && (topics.length == 1)) {
	            return new SNSTopic(Type.DESCRIPTOR, topics[0].getId(), topics[0].getBaseName(0).getBaseNameString().get_value());
	        }
	    }
	    return null;
    }
    
    private static void printTopic(Topic t) {
    	System.out.println("ID: "+t.getTopicID());
    	
		List<Topic> succList = t.getSuccessors();
		if (succList != null && !succList.isEmpty()) {
			for (Topic succ : succList) {
				System.out.print(" ");
				printTopic(succ);
			}
		}
    }

    static public class SNSTopicComparator implements Comparator<Topic> {
    	public final int compare(Topic topicA, Topic topicB) {
            try {
            	// Get the collator for the German Locale 
            	Collator gerCollator = Collator.getInstance(Locale.GERMAN);
            	return gerCollator.compare(topicA.getTopicName(), topicB.getTopicName());
            } catch (Exception e) {
                return 0;
            }
        }
    }
}
