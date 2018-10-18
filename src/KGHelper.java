import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * 
 * @author Bianca
 * Knowledge Graph Helper
 * 
 */

public class KGHelper {
	private final static String NAMESPACE = "http://projetobianca.com/";
	private final static String ID_PROPERTY = "http://projetobianca.com/id";
	private final static String NAME_PROPERTY = "http://projetobianca.com/name";
	private final static String BIRTHDATE_PROPERTY = "http://projetobianca.com/birthdate";
	private final static String NEWLINE = System.getProperty("line.separator");
	private final static String FILENAME = "triples.txt";
	
	public static String generateURI (String id) {
		return NAMESPACE+id;
	}
	
	public static StringBuffer generateTriples(String id, String name, String birthdate, String uri) {
		// defines subject of triples
		StringBuffer triples = new StringBuffer(uri);
		triples.append(NEWLINE);
		
		// first triple
		triples.append("    ");
		triples.append(ID_PROPERTY + " " + "\"" + id + "\";");
		triples.append(NEWLINE);

		// second triple
		triples.append("    ");
		triples.append(NAME_PROPERTY + " " +  "\"" + name + "\"");

		if (birthdate != null) {
			triples.append(";");
			triples.append(NEWLINE);

			// third triple is generated only if minister has birthdate
			triples.append("    ");
			triples.append(BIRTHDATE_PROPERTY + " " +  "\"" + birthdate + "\".");
			triples.append(NEWLINE);
		}
		else {
			triples.append(".");
			triples.append(NEWLINE);
		}
		return triples;
	}
	
	public static void writeToFile(String text) {
		BufferedWriter writer = null;
        try {

            //create a temporary file
        	String timeLog = FILENAME;
            File logFile = new File(timeLog);
            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
	}
}
