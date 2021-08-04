package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class roundslider_custom extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "caza.mosquito.roundslider_custom");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", caza.mosquito.roundslider_custom.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _vvvvvvvvvvvvvvvv3 = "";
public Object _vvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _vvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.B4XCanvas _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = 0;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = 0;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.B4XCanvas.B4XRect _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = 0;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = 0;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = 0;
public int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = 0;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.starter _vvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomofotos _vvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmlogin _vvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.dbutils _vvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.downloadservice _vvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.firebasemessaging _vvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmabout _vvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmaprender _vvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.frmcamara _vvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomotransmiten _vvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmcomovemos_app _frmcomovemos_app = null;
public caza.mosquito.frmcomovemos_enfermedades _frmcomovemos_enfermedades = null;
public caza.mosquito.frmcomovemos_porqueexiste _frmcomovemos_porqueexiste = null;
public caza.mosquito.frmdatosanteriores _vvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.frmdondecria _vvvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.frmeditprofile _vvvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.frmelmosquito _vvvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmenfermedades _vvvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmfotos _vvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.frmfotoscriadero _vvvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmidentificarmosquito _vvvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frminfografias_main _frminfografias_main = null;
public caza.mosquito.frminstrucciones _vvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.frmlocalizacion _vvvvvvvvvvvvvvvvvvvv7 = null;
public caza.mosquito.frmmapa _vvvvvvvvvvvvvvvvvvvv0 = null;
public caza.mosquito.frmpoliticadatos _vvvvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmquehacer _vvvvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.httputils2service _vvvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.multipartpost _vvvvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.register _vvvvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.uploadfiles _vvvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.utilidades _vvvvvvvvvvvvvvvvvvvvv7 = null;
public String  _base_resize(double _width,double _height) throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Private Sub Base_Resize (Width As Double, Height A";
 //BA.debugLineNum = 73;BA.debugLine="cvs.Resize(Width, Height)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Resize((float) (_width),(float) (_height));
 //BA.debugLineNum = 74;BA.debugLine="pnl.SetLayoutAnimated(0, 0, 0, Width, Height)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_width),(int) (_height));
 //BA.debugLineNum = 75;BA.debugLine="If thumb.IsInitialized = False Then CreateThumb";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.IsInitialized()==__c.False) { 
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2();};
 //BA.debugLineNum = 76;BA.debugLine="CircleRect.Initialize(ThumbSize + stroke, ThumbSi";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.Initialize((float) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2),(float) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2),(float) (_width-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2),(float) (_height-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 77;BA.debugLine="xlbl.SetLayoutAnimated(0, CircleRect.Left, Circle";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SetLayoutAnimated((int) (0),(int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getLeft()),(int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getTop()),(int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth()),(int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getHeight()));
 //BA.debugLineNum = 78;BA.debugLine="Draw";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Private mEventName As String 'ignore";
_vvvvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 9;BA.debugLine="Private mCallBack As Object 'ignore";
_vvvvvvvvvvvvvvvv4 = new Object();
 //BA.debugLineNum = 10;BA.debugLine="Private mBase As B4XView 'ignore";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Private xui As XUI 'ignore";
_vvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 12;BA.debugLine="Private cvs As B4XCanvas";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 13;BA.debugLine="Private mValue As Int = 75";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = (int) (75);
 //BA.debugLineNum = 14;BA.debugLine="Private mMin, mMax As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = 0;
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = 0;
 //BA.debugLineNum = 15;BA.debugLine="Private thumb As B4XBitmap";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Private pnl As B4XView";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private xlbl As B4XView";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private CircleRect As B4XRect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.B4XCanvas.B4XRect();
 //BA.debugLineNum = 19;BA.debugLine="Private ValueColor As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = 0;
 //BA.debugLineNum = 20;BA.debugLine="Public ThumbColor As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = 0;
 //BA.debugLineNum = 21;BA.debugLine="Private stroke As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = 0;
 //BA.debugLineNum = 22;BA.debugLine="Private ThumbSize As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = 0;
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2() throws Exception{
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _p = null;
int _r = 0;
int _g = 0;
int _l = 0;
 //BA.debugLineNum = 52;BA.debugLine="Private Sub CreateThumb";
 //BA.debugLineNum = 54;BA.debugLine="Dim p As B4XPath";
_p = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 55;BA.debugLine="Dim r As Int = 100dip";
_r = __c.DipToCurrent((int) (100));
 //BA.debugLineNum = 56;BA.debugLine="Dim g As Int = 4dip";
_g = __c.DipToCurrent((int) (4));
 //BA.debugLineNum = 57;BA.debugLine="Dim l As Int = 14dip";
_l = __c.DipToCurrent((int) (14));
 //BA.debugLineNum = 58;BA.debugLine="p.Initialize(r - l + g, 2 * r - 2dip + g)";
_p.Initialize((float) (_r-_l+_g),(float) (2*_r-__c.DipToCurrent((int) (2))+_g));
 //BA.debugLineNum = 59;BA.debugLine="p.LineTo(r + l + g, 2 * r - 2dip + g)";
_p.LineTo((float) (_r+_l+_g),(float) (2*_r-__c.DipToCurrent((int) (2))+_g));
 //BA.debugLineNum = 60;BA.debugLine="p.LineTo(r + g, 2 * r + l + g)";
_p.LineTo((float) (_r+_g),(float) (2*_r+_l+_g));
 //BA.debugLineNum = 61;BA.debugLine="p.LineTo(r - l + g, 2 * r - 2dip + g)";
_p.LineTo((float) (_r-_l+_g),(float) (2*_r-__c.DipToCurrent((int) (2))+_g));
 //BA.debugLineNum = 62;BA.debugLine="cvs.ClearRect(cvs.TargetRect)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.ClearRect(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getTargetRect());
 //BA.debugLineNum = 63;BA.debugLine="cvs.DrawPath(p, 0xFF5B5B5B, True, 0)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.DrawPath(_p,(int) (0xff5b5b5b),__c.True,(float) (0));
 //BA.debugLineNum = 64;BA.debugLine="cvs.DrawCircle(r + g, r + g, r, ThumbColor, True,";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.DrawCircle((float) (_r+_g),(float) (_r+_g),(float) (_r),_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1,__c.True,(float) (0));
 //BA.debugLineNum = 67;BA.debugLine="thumb = cvs.CreateBitmap.Crop(0, 0, 2 * r + g + 3";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.CreateBitmap().Crop((int) (0),(int) (0),(int) (2*_r+_g+__c.DipToCurrent((int) (3))),(int) (2*_r+_l+_g));
 //BA.debugLineNum = 68;BA.debugLine="ThumbSize = thumb.Height / 2";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getHeight()/(double)2);
 //BA.debugLineNum = 69;BA.debugLine="xlbl.SetTextAlignment(\"CENTER\", \"CENTER\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SetTextAlignment("CENTER","CENTER");
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public String  _designercreateview(Object _base,anywheresoftware.b4a.objects.LabelWrapper _lbl,anywheresoftware.b4a.objects.collections.Map _props) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Public Sub DesignerCreateView (Base As Object, Lbl";
 //BA.debugLineNum = 34;BA.debugLine="mBase = Base";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_base));
 //BA.debugLineNum = 35;BA.debugLine="cvs.Initialize(mBase)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7);
 //BA.debugLineNum = 36;BA.debugLine="mMin = Props.Get(\"Min\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = (int)(BA.ObjectToNumber(_props.Get((Object)("Min"))));
 //BA.debugLineNum = 37;BA.debugLine="mMax = Props.Get(\"Max\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = (int)(BA.ObjectToNumber(_props.Get((Object)("Max"))));
 //BA.debugLineNum = 38;BA.debugLine="pnl = xui.CreatePanel(\"pnl\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = _vvvvvvvvvvvvvvvv5.CreatePanel(ba,"pnl");
 //BA.debugLineNum = 39;BA.debugLine="xlbl = Lbl";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_lbl.getObject()));
 //BA.debugLineNum = 40;BA.debugLine="mBase.AddView(xlbl, 0, 0, 0, 0)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.AddView((android.view.View)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 41;BA.debugLine="mBase.AddView(pnl, 0, 0, 0, 0)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.AddView((android.view.View)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 42;BA.debugLine="ValueColor = xui.PaintOrColorToColor(Props.Get(\"V";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = _vvvvvvvvvvvvvvvv5.PaintOrColorToColor(_props.Get((Object)("ValueColor")));
 //BA.debugLineNum = 43;BA.debugLine="ThumbColor = xui.PaintOrColorToColor(Props.Get(\"T";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = _vvvvvvvvvvvvvvvv5.PaintOrColorToColor(_props.Get((Object)("ThumbColor")));
 //BA.debugLineNum = 44;BA.debugLine="If xui.IsB4A Or xui.IsB4i Then";
if (_vvvvvvvvvvvvvvvv5.getIsB4A() || _vvvvvvvvvvvvvvvv5.getIsB4i()) { 
 //BA.debugLineNum = 45;BA.debugLine="stroke = 100dip";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = __c.DipToCurrent((int) (100));
 }else if(_vvvvvvvvvvvvvvvv5.getIsB4J()) { 
 //BA.debugLineNum = 47;BA.debugLine="stroke = 8dip";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = __c.DipToCurrent((int) (8));
 };
 //BA.debugLineNum = 49;BA.debugLine="Base_Resize(mBase.Width, mBase.Height)";
_base_resize(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth(),_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getHeight());
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
int _radius = 0;
anywheresoftware.b4a.objects.B4XCanvas.B4XPath _p = null;
int _angle = 0;
int _b4jstrokeoffset = 0;
anywheresoftware.b4a.objects.B4XCanvas.B4XRect _dest = null;
int _r = 0;
int _cx = 0;
int _cy = 0;
 //BA.debugLineNum = 81;BA.debugLine="Private Sub Draw";
 //BA.debugLineNum = 82;BA.debugLine="cvs.ClearRect(cvs.TargetRect)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.ClearRect(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getTargetRect());
 //BA.debugLineNum = 83;BA.debugLine="Dim radius As Int = CircleRect.Width / 2";
_radius = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth()/(double)2);
 //BA.debugLineNum = 86;BA.debugLine="cvs.DrawCircle(CircleRect.CenterX, CircleRect.Cen";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.DrawCircle(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterX(),_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterY(),(float) (_radius),__c.Colors.Transparent,__c.False,(float) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 87;BA.debugLine="Dim p As B4XPath";
_p = new anywheresoftware.b4a.objects.B4XCanvas.B4XPath();
 //BA.debugLineNum = 88;BA.debugLine="Dim angle As Int = (mValue - mMin) / (mMax - mMin";
_angle = (int) ((_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2)/(double)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2)*360);
 //BA.debugLineNum = 89;BA.debugLine="Dim B4JStrokeOffset As Int";
_b4jstrokeoffset = 0;
 //BA.debugLineNum = 90;BA.debugLine="If xui.IsB4J Then B4JStrokeOffset = stroke / 2";
if (_vvvvvvvvvvvvvvvv5.getIsB4J()) { 
_b4jstrokeoffset = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2/(double)2);};
 //BA.debugLineNum = 91;BA.debugLine="p.InitializeArc(CircleRect.CenterX, CircleRect.Ce";
_p.InitializeArc(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterX(),_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterY(),(float) (_radius+_b4jstrokeoffset),(float) (-90),(float) (_angle));
 //BA.debugLineNum = 95;BA.debugLine="cvs.DrawCircle(CircleRect.CenterX, CircleRect.Cen";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.DrawCircle(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterX(),_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterY(),(float) (_radius-_b4jstrokeoffset),_vvvvvvvvvvvvvvvv5.Color_White,__c.True,(float) (0));
 //BA.debugLineNum = 96;BA.debugLine="Dim dest As B4XRect";
_dest = new anywheresoftware.b4a.objects.B4XCanvas.B4XRect();
 //BA.debugLineNum = 97;BA.debugLine="Dim r As Int = radius + ThumbSize / 2 + stroke /";
_r = (int) (_radius+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3/(double)2+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2/(double)2);
 //BA.debugLineNum = 98;BA.debugLine="Dim cx As Int = CircleRect.CenterX + r * CosD(ang";
_cx = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterX()+_r*__c.CosD(_angle-90));
 //BA.debugLineNum = 99;BA.debugLine="Dim cy As Int = CircleRect.CenterY + r * SinD(ang";
_cy = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterY()+_r*__c.SinD(_angle-90));
 //BA.debugLineNum = 100;BA.debugLine="dest.Initialize(cx - thumb.Width / 4, cy - ThumbS";
_dest.Initialize((float) (_cx-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getWidth()/(double)4),(float) (_cy-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3/(double)2),(float) (_cx+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getWidth()/(double)4),(float) (_cy+_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3/(double)2));
 //BA.debugLineNum = 101;BA.debugLine="cvs.DrawBitmapRotated(thumb, dest, angle)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.DrawBitmapRotated((android.graphics.Bitmap)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),_dest,(float) (_angle));
 //BA.debugLineNum = 102;BA.debugLine="cvs.Invalidate";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Invalidate();
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public int  _getvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Public Sub getValue As Int";
 //BA.debugLineNum = 132;BA.debugLine="Return mValue";
if (true) return _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1;
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return 0;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 25;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 26;BA.debugLine="mEventName = EventName";
_vvvvvvvvvvvvvvvv3 = _eventname;
 //BA.debugLineNum = 27;BA.debugLine="mCallBack = Callback";
_vvvvvvvvvvvvvvvv4 = _callback;
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public String  _pnl_touch(int _action,float _x,float _y) throws Exception{
int _dx = 0;
int _dy = 0;
float _dist = 0f;
int _angle = 0;
 //BA.debugLineNum = 106;BA.debugLine="Private Sub pnl_Touch (Action As Int, X As Float,";
 //BA.debugLineNum = 107;BA.debugLine="If Action = pnl.TOUCH_ACTION_MOVE_NOTOUCH Then Re";
if (_action==_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.TOUCH_ACTION_MOVE_NOTOUCH) { 
if (true) return "";};
 //BA.debugLineNum = 108;BA.debugLine="Dim dx As Int = x - CircleRect.CenterX";
_dx = (int) (_x-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterX());
 //BA.debugLineNum = 109;BA.debugLine="Dim dy As Int = y - CircleRect.CenterY";
_dy = (int) (_y-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getCenterY());
 //BA.debugLineNum = 110;BA.debugLine="Dim dist As Float = Sqrt(Power(dx, 2) + Power(dy,";
_dist = (float) (__c.Sqrt(__c.Power(_dx,2)+__c.Power(_dy,2)));
 //BA.debugLineNum = 111;BA.debugLine="If dist > CircleRect.Width / 2 Then";
if (_dist>_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.getWidth()/(double)2) { 
 //BA.debugLineNum = 112;BA.debugLine="Dim angle As Int = Round(ATan2D(dy, dx))";
_angle = (int) (__c.Round(__c.ATan2D(_dy,_dx)));
 //BA.debugLineNum = 113;BA.debugLine="angle = angle + 90";
_angle = (int) (_angle+90);
 //BA.debugLineNum = 114;BA.debugLine="angle = (angle + 360) Mod 360";
_angle = (int) ((_angle+360)%360);
 //BA.debugLineNum = 115;BA.debugLine="mValue = mMin + angle / 360 * (mMax - mMin)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = (int) (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2+_angle/(double)360*(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3-_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2));
 //BA.debugLineNum = 116;BA.debugLine="mValue = Max(mMin, Min(mMax, mValue))";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = (int) (__c.Max(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2,__c.Min(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1)));
 //BA.debugLineNum = 118;BA.debugLine="CreateThumb";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2();
 //BA.debugLineNum = 119;BA.debugLine="Draw";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 120;BA.debugLine="If SubExists(mCallBack, mEventName & \"_ValueChan";
if (__c.SubExists(ba,_vvvvvvvvvvvvvvvv4,_vvvvvvvvvvvvvvvv3+"_ValueChanged")) { 
 //BA.debugLineNum = 121;BA.debugLine="CallSub2(mCallBack, mEventName & \"_ValueChanged";
__c.CallSubNew2(ba,_vvvvvvvvvvvvvvvv4,_vvvvvvvvvvvvvvvv3+"_ValueChanged",(Object)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1));
 };
 };
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public String  _setvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6(int _v) throws Exception{
 //BA.debugLineNum = 126;BA.debugLine="Public Sub setValue (v As Int)";
 //BA.debugLineNum = 127;BA.debugLine="mValue = Max(mMin, Min(mMax, v))";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = (int) (__c.Max(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2,__c.Min(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3,_v)));
 //BA.debugLineNum = 128;BA.debugLine="Draw";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
