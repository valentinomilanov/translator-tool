package com.alas.translator;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.faces.bean.ManagedBean;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;

import com.alas.translator.XmlWriter;;


@ManagedBean (name="translator")
public class TranslatorBean {
	private String toTranslate;
	private String translated;
	
	public TranslatorBean() {
		
	}

	public void translateText() throws GeneralSecurityException, IOException {
		
		Translate t = new Translate.Builder(
                GoogleNetHttpTransport.newTrustedTransport()
                , GsonFactory.getDefaultInstance(), null).setApplicationName("transtor-tool").build();
		Translate.Translations.List list = t.new Translations().list(Arrays.asList(toTranslate), "EN");
		list.setKey("AIzaSyAmcCWBau-AlLwsxbZGSmwkSOjJ9l2RBqM");
		TranslationsListResponse response = list.execute();
		for(TranslationsResource translationsResource : response.getTranslations()) {
			translated = translationsResource.getTranslatedText();
		}
	}
	
	public String getTranslatedText() throws GeneralSecurityException, IOException {
		translateText();
		return translated;
	}
	
	public String finalTranslate() throws GeneralSecurityException, IOException {
		translateText();
		new XmlWriter(toTranslate, translated);
		return "result";
	}
	
// Getters and setters
	public String getToTranslate() {
		return toTranslate;
	}

	public void setToTranslate(String toTranslate) throws GeneralSecurityException, IOException {
		this.toTranslate = toTranslate;
		translateText();
	}

	public String getTranslated() {
		return translated;
	}

	public void setTranslated(String translated) {
		this.translated = translated;
	}
	
	
}
