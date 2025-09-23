package com.example.common.config.api;

import com.example.common.util.CommonUtil;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
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
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false) {
			@Override
			public void setAsText(String text) {
				super.setAsText(CommonUtil.safeTrim(text));
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
			protected Object convertElement(Object element) {
				if (element instanceof String text) return text.strip();
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