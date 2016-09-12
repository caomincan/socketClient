import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class studentClient {
    private Socket myClient;
    private String HostName;
    
    public studentClient(String serverAddress,int PortNumber) throws UnknownHostException, IOException{
    	this.myClient = new Socket(serverAddress,PortNumber);
    	this.HostName = serverAddress;
    }
    
    public void request(String file) throws IOException{
    	System.out.println("Send Request to Server:");
    	PrintWriter out = new PrintWriter(myClient.getOutputStream(),true);
    	out.println("GET /"+file+" HTTP/1.0");
    	out.println("Host: "+this.HostName);
    	out.println("\r\n");
    	System.out.println("GET /"+file+" HTTP/1.0");
    	System.out.println("Host: "+this.HostName);
    	System.out.println("\r\n");
    }
    
    public void close() throws IOException{
    	this.myClient.close();
    }
    public List<String> getResponse() throws IOException{
    	System.out.println("Get Response from Server:");
    	List<String> response = new LinkedList<String>();
    	BufferedReader in = new BufferedReader(new InputStreamReader(this.myClient.getInputStream()));
    	String line = null;
    	while((line=in.readLine()) != null){
    		response.add(line);
    		System.out.println("    "+line);
    	}
    	return response;
    }
    
    public void saveFile(String fileName,List<String> response) throws IOException{
    	if(response.size()>=4){
    		String head = response.get(0);
    		if(head.contains("OK")){
    			File newFile = new File(fileName);
    	    	FileOutputStream fs = new FileOutputStream(newFile);
    	    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fs));
    	    	for(int i=3;i<response.size();i++){
    	    		writer.write(response.get(i)+"\r\n");
    	    	}
    	    	writer.close();
    		}
    	}
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        if(args.length != 2){
        	System.out.println("Wrong Usage. Usage: java studentClient <HostName> <PortNumber>");
        }else{
        	try{
        		BufferedReader sys = new BufferedReader(new InputStreamReader(System.in));
        		studentClient client = new studentClient(args[0],Integer.valueOf(args[1]).intValue());
        		System.out.print("Please input file name: ");
        		String requestFileName = sys.readLine();
        		// Send Request to server
        		client.request(requestFileName);
        		// Get Response from server
        		List<String> response = client.getResponse();
        		// Save the file
        		client.saveFile(requestFileName, response);
        		
        	} catch(UnknownHostException unknow){
        		System.out.println("Unknow Host.");
        	} catch(IOException e){
        		e.printStackTrace();
        	}
        }
	}

}
