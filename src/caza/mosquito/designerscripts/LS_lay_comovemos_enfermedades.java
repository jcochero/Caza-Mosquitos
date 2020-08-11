package caza.mosquito.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_lay_comovemos_enfermedades{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
views.get("but_backenfermedades").vw.setLeft((int)((50d / 100 * width)-(views.get("but_backenfermedades").vw.getWidth())/2d));
//BA.debugLineNum = 4;BA.debugLine="but_FiebreAmarilla.Left = 50%x - (but_FiebreAmarilla.Width * 2)"[lay_comovemos_enfermedades/General script]
views.get("but_fiebreamarilla").vw.setLeft((int)((50d / 100 * width)-((views.get("but_fiebreamarilla").vw.getWidth())*2d)));
//BA.debugLineNum = 5;BA.debugLine="but_Chiku.Left = but_FiebreAmarilla.Left + but_FiebreAmarilla.Width"[lay_comovemos_enfermedades/General script]
views.get("but_chiku").vw.setLeft((int)((views.get("but_fiebreamarilla").vw.getLeft())+(views.get("but_fiebreamarilla").vw.getWidth())));
//BA.debugLineNum = 6;BA.debugLine="but_Dengue.Left = but_Chiku.Left + but_Chiku.Width"[lay_comovemos_enfermedades/General script]
views.get("but_dengue").vw.setLeft((int)((views.get("but_chiku").vw.getLeft())+(views.get("but_chiku").vw.getWidth())));
//BA.debugLineNum = 7;BA.debugLine="but_Zika.Left = but_Dengue.Left + but_Dengue.Width"[lay_comovemos_enfermedades/General script]
views.get("but_zika").vw.setLeft((int)((views.get("but_dengue").vw.getLeft())+(views.get("but_dengue").vw.getWidth())));
//BA.debugLineNum = 8;BA.debugLine="lblArrow_Dengue.Left = but_Dengue.Left + but_Dengue.width / 2"[lay_comovemos_enfermedades/General script]
views.get("lblarrow_dengue").vw.setLeft((int)((views.get("but_dengue").vw.getLeft())+(views.get("but_dengue").vw.getWidth())/2d));
//BA.debugLineNum = 9;BA.debugLine="lblArrow_Fiebre.Left = but_FiebreAmarilla.Left + but_FiebreAmarilla.width / 2"[lay_comovemos_enfermedades/General script]
views.get("lblarrow_fiebre").vw.setLeft((int)((views.get("but_fiebreamarilla").vw.getLeft())+(views.get("but_fiebreamarilla").vw.getWidth())/2d));
//BA.debugLineNum = 10;BA.debugLine="Label3.Left = but_Chiku.Left + but_Chiku.width / 2"[lay_comovemos_enfermedades/General script]
views.get("label3").vw.setLeft((int)((views.get("but_chiku").vw.getLeft())+(views.get("but_chiku").vw.getWidth())/2d));
//BA.debugLineNum = 11;BA.debugLine="label4.Left = but_Zika.Left + but_Zika.width / 2"[lay_comovemos_enfermedades/General script]
views.get("label4").vw.setLeft((int)((views.get("but_zika").vw.getLeft())+(views.get("but_zika").vw.getWidth())/2d));

}
}