package org.meandre.components.fedora.support;

public class CorpusObject 
{
	
   private String fedora_pid = null;
   private String fedora_title = null;
   
   public CorpusObject()
   {}
   
   public CorpusObject(String pid)
   {
	 fedora_pid = pid;
   }
   
   public String getFedoraPid()
   {
	   return fedora_pid;
   }
   
   public void setTitle(String title)
   {
	   fedora_title = title;
   }
   
   public String getTitle()
   { return fedora_title; }
   
}
