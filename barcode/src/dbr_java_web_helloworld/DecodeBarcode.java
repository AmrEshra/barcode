package dbr_java_web_helloworld;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dynamsoft.barcode.jni.BarcodeReader;
import com.dynamsoft.barcode.jni.TextResult;
import com.google.gson.Gson;

@MultipartConfig
public class DecodeBarcode extends HttpServlet{
	
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter responseWriter = response.getWriter();
		
		Part imgPart = request.getPart("img");
		String resultPath = request.getParameter("path");
		
		try {
			// if licenseKeys expire, visit https://www.dynamsoft.com/CustomerPortal/Portal/TrialLicense.aspx to get a try key
			BarcodeReader reader = new BarcodeReader("t0171ZQYAAFfE5nZPARmYzhz5Qcu+klcN+hNsBUWpR2YGk/ccHu702APj+DU+5Unh8jJJpe18LZG75jw2DZizretDJdQX7ed9s783jd/Z32ZU3wg0Bk1AcxgKGAoYChgKGAoYChgKGAoYChgaGBoYGhgaGBoYGhgaGBoYGhgGGAYYBhgGGAYYBhgGGAYY9rvvP7U3BE36ZlTX+FN1hk0j0Bg0+d2sba65xU8yMEPP");
			
			// what's template? visit https://www.dynamsoft.com/help/Barcode-Reader/CustomizeTemplate.html
			String exampleTpl = "exampleTpl";
			reader.appendParameterTemplate("{\"ImageParameters\": {\"Name\": \""+exampleTpl+"\",\"BarcodeFormatIds\": [\"All\"],\"ExpectedBarcodesCount\": 512,\"DeblurLevel\": 9,\"AntiDamageLevel\": 9,\"TextFilterMode\": \"Enable\"}}");
			TextResult[] results = reader.decodeFileInMemory(imgPart.getInputStream(), "exampleTpl");
			
			
			BufferedWriter out = null;
			try  
			{
			    FileWriter fstream = new FileWriter(resultPath + "result.txt", true); //true tells to append data.
			    out = new BufferedWriter(fstream);
			    System.out.println("-----------------------------");
			    
				for (int i = 0 ; i < results.length ; i++) {
	
					System.out.println(imgPart.getSubmittedFileName().substring(imgPart.getSubmittedFileName().indexOf('/') + 1) + "," +
								results[i].barcodeText.replace('\\', ','));
					
					out.write(imgPart.getSubmittedFileName().substring(imgPart.getSubmittedFileName().indexOf('/') + 1) + "," +
							results[i].barcodeText.replace('\\', ','));
					out.newLine();
				}
			}
			catch (IOException e)
			{
			    System.err.println("Error: " + e.getMessage());
			}
			finally
			{
			    if(out != null) {
			        out.close();
			    }
			}
			
			responseWriter.write(new Gson().toJson(results));
		} catch (Exception e) {
			responseWriter.write(new Gson().toJson(e));
		}finally{
			responseWriter.close();
		}
	}
}
