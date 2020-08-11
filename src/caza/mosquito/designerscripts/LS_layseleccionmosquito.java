package caza.mosquito.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_layseleccionmosquito{

public static void LS_480x320_1(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("lblabdomen").vw.setWidth((int)((views.get("lblfondo").vw.getWidth())/4d));
views.get("lbltorax").vw.setWidth((int)((views.get("lblfondo").vw.getWidth())/4d));
views.get("lblcabeza").vw.setWidth((int)((views.get("lblfondo").vw.getWidth())/4d));
views.get("lblpatas").vw.setWidth((int)((views.get("lblfondo").vw.getWidth())/4d));
views.get("lblabdomen").vw.setLeft((int)((views.get("lblfondo").vw.getWidth())-(views.get("lblabdomen").vw.getWidth())));
views.get("lblpatas").vw.setLeft((int)((views.get("lblabdomen").vw.getLeft())-(views.get("lblpatas").vw.getWidth())));
views.get("lbltorax").vw.setLeft((int)((views.get("lblpatas").vw.getLeft())-(views.get("lbltorax").vw.getWidth())));
views.get("lblcabeza").vw.setLeft((int)((views.get("lbltorax").vw.getLeft())-(views.get("lblcabeza").vw.getWidth())));
views.get("lblespecie").vw.setWidth((int)((100d / 100 * width)-(views.get("scrpartes").vw.getWidth())));
//BA.debugLineNum = 11;BA.debugLine="btnOkSeleccionMosquito.Width = lblEspecie.Width"[laySeleccionMosquito/480x320,scale=1]
views.get("btnokseleccionmosquito").vw.setWidth((int)((views.get("lblespecie").vw.getWidth())));
//BA.debugLineNum = 12;BA.debugLine="imgMosquito.Left = lblEspecie.Left + (lblEspecie.Width / 2) - (imgMosquito.Width / 2)"[laySeleccionMosquito/480x320,scale=1]
views.get("imgmosquito").vw.setLeft((int)((views.get("lblespecie").vw.getLeft())+((views.get("lblespecie").vw.getWidth())/2d)-((views.get("imgmosquito").vw.getWidth())/2d)));
//BA.debugLineNum = 13;BA.debugLine="btnNoSe.Width = lblFondo.Width"[laySeleccionMosquito/480x320,scale=1]
views.get("btnnose").vw.setWidth((int)((views.get("lblfondo").vw.getWidth())));

}
public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);

}
}