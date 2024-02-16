import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class BookForXML {
	public static void main(String[] args) throws Exception {
		
		String url = "jdbc:mysql://127.0.0.1:3333/task01books";
		String userName ="root";
		String passWord ="";
		
		 
		
		// Establishing the connection
		Connection con = DriverManager.getConnection(url,userName,passWord);
		
		
		

		
		
		File inputFile = new File("src\\books.xml");
		
		// Create a DocumentBuilder
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			// Parse the XML file to create a Document object
			Document document = dBuilder.parse(inputFile);
			
			// Normalize the document
            document.getDocumentElement().normalize();
            
            // Get the root element
            Element rootElement = document.getDocumentElement();
            
            // Get all book nodes
            NodeList bookList = rootElement.getElementsByTagName("book");
            
            // Iterate through each book node
            for (int i = 0; i < bookList.getLength(); i++) {
            	Node bookNode = bookList.item(i);
            	
            	if (bookNode.getNodeType() == Node.ELEMENT_NODE) {
            		Element bookElement = (Element) bookNode;
            		
            		// Retrieve book details
                    String title = bookElement.getElementsByTagName("title").item(0).getTextContent();
                    String author = bookElement.getElementsByTagName("author").item(0).getTextContent();
                    String genre = bookElement.getElementsByTagName("genre").item(0).getTextContent();
                    double price = Double.parseDouble(bookElement.getElementsByTagName("price").item(0).getTextContent());
                    String publication_date = bookElement.getElementsByTagName("publication_date").item(0).getTextContent();
                    String isbn = bookElement.getElementsByTagName("isbn").item(0).getTextContent();
                    
                    SaveToDatabase(con,title,author,genre,price,publication_date,isbn);
                    
//                    // Display book details
//                    System.out.println("Title: " + title);
//                    System.out.println("Author: " + author);
//                    System.out.println("Genre: " + genre);
//                    System.out.println("Price: " + price);
//                    System.out.println("Publication Date: " + publication_date);
//                    System.out.println("ISBN: " + isbn);
//                    System.out.println();
                    
            		
            	}
            	
            	
            }


            
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}


	private static void SaveToDatabase(Connection con, String title, String author, String genre, double price, String publication_date, String isbn ) throws SQLException {
		// SQL query to insert data
				String sql = "INSERT INTO books(title, author, genre, price, publication_date, isbn) VALUE(?, ?, ?, ?, ?, ?)";
				
		// Creating a PreparedStatement with the SQL query
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        
        // Setting values for the placeholder in the SQL query
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, author);
        preparedStatement.setString(3, genre);
        preparedStatement.setDouble(4, price);
        preparedStatement.setString(5, publication_date);
        preparedStatement.setString(6, isbn);

        // Executing the SQL query to insert data
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Data inserted successfully!");
        } else {
            System.out.println("Failed to insert data.");
        }
	}

}
