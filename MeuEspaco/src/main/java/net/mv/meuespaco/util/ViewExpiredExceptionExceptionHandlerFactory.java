package net.mv.meuespaco.util;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * @author javaserverfowner
 * @see https://community.oracle.com/blogs/edburns/2009/09/03/dealing-gracefully-viewexpiredexception-jsf2
 * @created Postado por javaserverfowner em edburns em 03/09/2009 13:55:00
 */
public class ViewExpiredExceptionExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;
	 
    public ViewExpiredExceptionExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
 
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler result = parent.getExceptionHandler();
        result = new ViewExpiredExceptionExceptionHandler(result);
 
        return result;
    }
	
}
