package net.mv.meuespaco.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Stateless
public class FotoHelper {

	String imagePath = IConstants.CAMINHO_DAS_FOTOS;
	
	public void salvaFotoEmArquivo(UploadedFile uploadedFile) throws IOException {
		
		String imageName = uploadedFile.getFileName();

		File destinationFile = new File(imagePath, URLDecoder.decode(imageName, "UTF-8"));
		
		byte[] imageContents = uploadedFile.getContents(); 
		
        FileOutputStream fos = new FileOutputStream(destinationFile.getAbsolutePath());  
        
        fos.write(imageContents);  
        fos.close();  
		
	}
	
	public StreamedContent getFoto() throws FileNotFoundException, UnsupportedEncodingException {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params = fc.getExternalContext().getRequestHeaderMap();
		String nomeDaFoto = params.get("nomeDaFoto");
		
		File file = null;
		
		file = new File(imagePath, URLDecoder.decode(nomeDaFoto, "UTF-8"));
		
		InputStream stream = new FileInputStream(file);
		StreamedContent streamed = new DefaultStreamedContent(stream, "image/jpeg");
		
		return streamed;
		
	}
}
