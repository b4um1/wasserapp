/*
 * 
 */
package at.fhhgb.mc.wasserapp.rssfeed;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedParser.
 */
public class FeedParser {

	/** The factory. */
	private SAXParserFactory factory;

	/**
	 * Instantiates a new feed parser.
	 */
	public FeedParser() {
		factory = SAXParserFactory.newInstance();
	}

	/**
	 * Parses the.
	 *
	 * @param in the in
	 * @return the feed rss
	 * @throws Exception the exception
	 */
	public FeedRss parse(InputStream in) throws Exception {
		SAXParser parser = factory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		RSSHandler rss = new RSSHandler();
		reader.setContentHandler(rss);
		reader.parse(new InputSource(in));
		return rss.result;
	}

	/**
	 * The Class RSSHandler.
	 */
	class RSSHandler extends DefaultHandler {
		
		/** The result. */
		FeedRss result = new FeedRss();

		/** The el. */
		private String el;
		
		/** The url. */
		private String url;
		
		/** The title. */
		private StringBuilder title = new StringBuilder();

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("item".equals(qName)) {
				title.setLength(0);
				url = null;
			} else if ("enclosure".equals(qName)) {
				url = attributes.getValue("url");
			}
			el = localName;
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if ("title".equals(el))
				title.append(ch, start, length);
		}

		/* (non-Javadoc)
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("item".equals(localName) && url != null) {
				result.add(url, title.toString());
			}
		}
	}

}
