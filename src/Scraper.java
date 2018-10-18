import java.io.IOException;
import java.net.MalformedURLException;

import javax.script.ScriptException;
import javax.sound.sampled.LineUnavailableException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

import java.util.Calendar;
import java.util.List;

public class Scraper {


	public static void main(String[] args){		
	    // turn off htmlunit warnings
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		try {
			startScraping();
		} catch (FailingHttpStatusCodeException | IOException | ScriptException | InterruptedException
				| LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static void startScraping() throws FailingHttpStatusCodeException, MalformedURLException, IOException, ScriptException, InterruptedException, LineUnavailableException {
		try (final WebClient webClient = new WebClient()) {
			// StringBuffer for appending triples as ministers are analyzed
			StringBuffer triples = new StringBuffer();
			
			// Get minister list page
			final HtmlPage ministersPage = webClient.getPage("http://www.stj.jus.br/web/verMinistrosSTJ?parametro=1");
			List<HtmlSpan> nodes = ministersPage.getByXPath("//*[@class=\"clsMinistrosNome\"]");
			// First item is table header
			nodes.remove(0);
			// each node is a minister
			for (HtmlSpan node : nodes) {
				// gets minister's name
				String name = node.asText();
				
				// gets minister's id
				HtmlAnchor anchor = (HtmlAnchor) node.getFirstChild();
				String url = anchor.getAttribute("href");
				String id = url.replace("verCurriculoMinistro?parametro=1&cod_matriculamin=", "");
				String uri = KGHelper.generateURI(id);
				
				// now, we need to go to each minister's page to get his/her birth date
				Calendar birthdate = getBirthDate(anchor.click(), name);
				String stringDate = Helper.convertCalendarToString(birthdate);
				
				// triples generation for each minister/node
				triples.append(KGHelper.generateTriples(id, name, stringDate, uri));
				
				System.out.println(name + " - " + stringDate);
			}
			// at the end of the loop, all ministers have been added to the triples variable
			KGHelper.writeToFile(triples.toString());
		}
	}
	
	// used to get birth date information
	private static Calendar getBirthDate(HtmlPage page, String name) throws IOException {
		List<DomElement> elements = page.getByXPath("//*[contains(text(),'Nascimento')"
				+ "or contains(text(),'nascimento')]");
		if (!elements.isEmpty()) {
			DomNode birthText = elements.get(0).getParentNode();
			Calendar birthdate = Helper.findBirthDate(birthText.asText());
			if (birthdate != null) {
				//System.out.println(birthdate + " - " + name);
				return birthdate;
			}
			else {
				DomNode birthTextOtherPossibility = birthText.getParentNode();
				birthdate = Helper.findBirthDate(birthTextOtherPossibility.asText());
				if (birthdate != null) {
					//System.out.println(birthdate + " - " + name);
					return birthdate;
				}
				else {
					//System.out.println("Não achou birthdate1 de: " + name);
					return null;
				}
			}
		}
		else {
			//System.out.println("Não achou birthdate2 de: " + name);
			return null;
		}
	}
	

	

}
