package caza.mosquito.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_lay_enfermedades_sintomas{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("imgsintoma").vw.setLeft((int)((15d / 100 * width)));
views.get("lblsintoma_descripcion").vw.setLeft((int)((15d / 100 * width)));
views.get("lblsintoma_titulo").vw.setLeft((int)((15d / 100 * width)));
views.get("btncerrarsintoma").vw.setLeft((int)((65d / 100 * width)));

}
}