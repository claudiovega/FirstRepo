package cl.propiedades.util;

import java.util.List;

public class ListUtils {

	public ListUtils() {
		// TODO Auto-generated constructor stub
	}
	public static <T> void fromArrayToList(T[] a, List<T> l){
		for(T o : a){
			l.add(o);
		}
	}
}
