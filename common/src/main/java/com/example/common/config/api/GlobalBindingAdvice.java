package com.example.common.config.api;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalBindingAdvice {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 단일 String
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true) {
			@Override
			public void setAsText(String text) {
				super.setAsText(text != null ? text.trim() : null);
			}
		});

		// List 처리
		binder.registerCustomEditor(List.class, new CustomCollectionEditor(List.class) {
			@Override
			public void setValue(Object value) {
				List<?> list = value == null ? new ArrayList<>() : (List<?>) value;
				TrimUtil.trimObject(list);
				super.setValue(list);
			}

			@Override
			@NonNull
			protected Object convertElement(@NonNull Object element) {
				if (element instanceof String s) return s.trim();
				return element;
			}
		});

		// Map 처리
		binder.registerCustomEditor(Map.class, new CustomMapEditor(Map.class) {
			@Override
			public void setValue(Object value) {
				Map<?, ?> map = value == null ? new HashMap<>() : (Map<?, ?>) value;
				TrimUtil.trimObject(map);
				super.setValue(map);
			}
		});
	}
}