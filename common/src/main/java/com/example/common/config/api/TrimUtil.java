package com.example.common.config.api;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TrimUtil {

	// DTO 클래스별 String/필드 캐시
	private static final Map<Class<?>, Field[]> stringFieldsCache = new ConcurrentHashMap<>();

	/**
	 * 안전하게 DTO, Map, List 내부까지 재귀적으로 trim
	 * 순환 참조 객체는 Set 으로 체크하여 무한 재귀 방지
	 */
	public static void trimObject(Object obj) {
		trimObject(obj, Collections.newSetFromMap(new IdentityHashMap<>()));
	}

	@SuppressWarnings("unchecked")
	private static void trimObject(Object obj, Set<Object> visited) {
		if (obj == null || visited.contains(obj)) return;

		visited.add(obj);

		if (obj instanceof String) {
			// ignore
			// 단일 String 객체는 외부에서 처리
		} else if (obj instanceof List<?> list) {
			List<Object> mutableList = (List<Object>) list;
			for (int i = 0; i < mutableList.size(); i++) {
				Object v = mutableList.get(i);
				if (v instanceof String str) {
					mutableList.set(i, str.strip());
				} else {
					trimObject(v, visited);
				}
			}
		} else if (obj instanceof Map<?, ?> map) {
			Map<Object, Object> mutableMap = (Map<Object, Object>) map;
			Map<Object, Object> trimmedMap = new HashMap<>(mutableMap.size());
			for (Map.Entry<Object, Object> entry : mutableMap.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();

				if (key instanceof String ks) key = ks.strip();

				if (value instanceof String vs) value = vs.strip();
				else trimObject(value, visited);

				trimmedMap.put(key, value);
			}
			mutableMap.clear();
			mutableMap.putAll(trimmedMap);
		} else {
			// DTO 처리
			Field[] fields = stringFieldsCache.computeIfAbsent(obj.getClass(), cls ->
					Arrays.stream(cls.getDeclaredFields())
							.filter(f -> !Modifier.isStatic(f.getModifiers()))
							.toArray(Field[]::new)
			);

			for (Field f : fields) {
				f.setAccessible(true);
				try {
					Object value = f.get(obj);
					if (value instanceof String s) {
						f.set(obj, s.strip());
					} else {
						trimObject(value, visited);
					}
				} catch (IllegalAccessException ignored) {}
			}
		}

		visited.remove(obj);
	}
}