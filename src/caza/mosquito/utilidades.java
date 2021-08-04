package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class utilidades {
private static utilidades mostCurrent = new utilidades();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4h.okhttp.OkHttpClientWrapper _vvvvvv1 = null;
public static anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _vvvvvv2 = null;
public static int _vvvvvvvv0 = 0;
public static anywheresoftware.b4a.objects.Accessibility.Accessibility2 _vvvvvvvvv1 = null;
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
public static String  _vvvvvvvvv2(anywheresoftware.b4a.BA _ba,String _entrada) throws Exception{
 //BA.debugLineNum = 146;BA.debugLine="Sub convertNulltoString(entrada As String)";
 //BA.debugLineNum = 147;BA.debugLine="If entrada <> Null Then";
if (_entrada!= null) { 
 //BA.debugLineNum = 148;BA.debugLine="Return entrada";
if (true) return _entrada;
 }else {
 //BA.debugLineNum = 150;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 153;BA.debugLine="End Sub";
return "";
}
public static void  _vvvvvvvvv3(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _parent,anywheresoftware.b4a.objects.ButtonWrapper _objeto,int _clr) throws Exception{
ResumableSub_CreateHaloEffect rsub = new ResumableSub_CreateHaloEffect(null,_ba,_parent,_objeto,_clr);
rsub.resume((_ba.processBA == null ? _ba : _ba.processBA), null);
}
public static class ResumableSub_CreateHaloEffect extends BA.ResumableSub {
public ResumableSub_CreateHaloEffect(caza.mosquito.utilidades parent,anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _parent,anywheresoftware.b4a.objects.ButtonWrapper _objeto,int _clr) {
this.parent = parent;
this._ba = _ba;
this._parent = _parent;
this._objeto = _objeto;
this._clr = _clr;
}
caza.mosquito.utilidades parent;
anywheresoftware.b4a.BA _ba;
anywheresoftware.b4a.objects.B4XViewWrapper _parent;
anywheresoftware.b4a.objects.ButtonWrapper _objeto;
int _clr;
anywheresoftware.b4a.objects.B4XCanvas _cvs = null;
anywheresoftware.b4a.objects.B4XViewWrapper.XUI _xui = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _radius = 0;
int _x = 0;
int _y = 0;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp = null;
int _i = 0;
int step12;
int limit12;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 165;BA.debugLine="Dim cvs As B4XCanvas";
_cvs = new anywheresoftware.b4a.objects.B4XCanvas();
 //BA.debugLineNum = 166;BA.debugLine="Dim xui As XUI";
_xui = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 167;BA.debugLine="Dim p As B4XView = xui.CreatePanel(\"\")";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = _xui.CreatePanel((_ba.processBA == null ? _ba : _ba.processBA),"");
 //BA.debugLineNum = 168;BA.debugLine="Dim radius As Int = 150dip";
_radius = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (150));
 //BA.debugLineNum = 169;BA.debugLine="Dim x,y As Int";
_x = 0;
_y = 0;
 //BA.debugLineNum = 170;BA.debugLine="x = objeto.left + (objeto.Width / 2)";
_x = (int) (_objeto.getLeft()+(_objeto.getWidth()/(double)2));
 //BA.debugLineNum = 171;BA.debugLine="y = objeto.top + (objeto.Height / 2)";
_y = (int) (_objeto.getTop()+(_objeto.getHeight()/(double)2));
 //BA.debugLineNum = 173;BA.debugLine="p.SetLayoutAnimated(0, 0, 0, radius * 2, radius *";
_p.SetLayoutAnimated((int) (0),(int) (0),(int) (0),(int) (_radius*2),(int) (_radius*2));
 //BA.debugLineNum = 174;BA.debugLine="cvs.Initialize(p)";
_cvs.Initialize(_p);
 //BA.debugLineNum = 175;BA.debugLine="cvs.DrawCircle(cvs.TargetRect.CenterX, cvs.Target";
_cvs.DrawCircle(_cvs.getTargetRect().getCenterX(),_cvs.getTargetRect().getCenterY(),(float) (_cvs.getTargetRect().getWidth()/(double)2),_clr,anywheresoftware.b4a.keywords.Common.True,(float) (0));
 //BA.debugLineNum = 176;BA.debugLine="Dim bmp As B4XBitmap = cvs.CreateBitmap";
_bmp = new anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper();
_bmp = _cvs.CreateBitmap();
 //BA.debugLineNum = 177;BA.debugLine="For i = 1 To 2";
if (true) break;

case 1:
//for
this.state = 4;
step12 = 1;
limit12 = (int) (2);
_i = (int) (1) ;
this.state = 5;
if (true) break;

case 5:
//C
this.state = 4;
if ((step12 > 0 && _i <= limit12) || (step12 < 0 && _i >= limit12)) this.state = 3;
if (true) break;

case 6:
//C
this.state = 5;
_i = ((int)(0 + _i + step12)) ;
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 178;BA.debugLine="CreateHaloEffectHelper(Parent,bmp, x, y, clr, ra";
_vvvvvvvvv4(_ba,_parent,_bmp,_x,_y,_clr,_radius);
 //BA.debugLineNum = 179;BA.debugLine="Sleep(200)";
anywheresoftware.b4a.keywords.Common.Sleep(_ba,this,(int) (200));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 if (true) break;
if (true) break;

case 4:
//C
this.state = -1;
;
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _vvvvvvvvv4(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _parent,anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp,int _x,int _y,int _clr,int _radius) throws Exception{
ResumableSub_CreateHaloEffectHelper rsub = new ResumableSub_CreateHaloEffectHelper(null,_ba,_parent,_bmp,_x,_y,_clr,_radius);
rsub.resume((_ba.processBA == null ? _ba : _ba.processBA), null);
}
public static class ResumableSub_CreateHaloEffectHelper extends BA.ResumableSub {
public ResumableSub_CreateHaloEffectHelper(caza.mosquito.utilidades parent,anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.B4XViewWrapper _parent,anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp,int _x,int _y,int _clr,int _radius) {
this.parent = parent;
this._ba = _ba;
this._parent = _parent;
this._bmp = _bmp;
this._x = _x;
this._y = _y;
this._clr = _clr;
this._radius = _radius;
}
caza.mosquito.utilidades parent;
anywheresoftware.b4a.BA _ba;
anywheresoftware.b4a.objects.B4XViewWrapper _parent;
anywheresoftware.b4a.objects.B4XViewWrapper.B4XBitmapWrapper _bmp;
int _x;
int _y;
int _clr;
int _radius;
anywheresoftware.b4a.objects.ImageViewWrapper _iv = null;
anywheresoftware.b4a.objects.B4XViewWrapper _p = null;
int _duration = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = -1;
 //BA.debugLineNum = 184;BA.debugLine="Dim iv As ImageView";
_iv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 185;BA.debugLine="iv.Initialize(\"\")";
_iv.Initialize(_ba,"");
 //BA.debugLineNum = 186;BA.debugLine="Dim p As B4XView = iv";
_p = new anywheresoftware.b4a.objects.B4XViewWrapper();
_p = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_iv.getObject()));
 //BA.debugLineNum = 187;BA.debugLine="p.SetBitmap(bmp)";
_p.SetBitmap((android.graphics.Bitmap)(_bmp.getObject()));
 //BA.debugLineNum = 188;BA.debugLine="Parent.AddView(p, x, y, 0, 0)";
_parent.AddView((android.view.View)(_p.getObject()),_x,_y,(int) (0),(int) (0));
 //BA.debugLineNum = 189;BA.debugLine="Dim duration As Int = 1000";
_duration = (int) (1000);
 //BA.debugLineNum = 190;BA.debugLine="p.SetLayoutAnimated(duration, x - radius, y - rad";
_p.SetLayoutAnimated(_duration,(int) (_x-_radius),(int) (_y-_radius),(int) (2*_radius),(int) (2*_radius));
 //BA.debugLineNum = 191;BA.debugLine="p.SetVisibleAnimated(duration, False)";
_p.SetVisibleAnimated(_duration,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 192;BA.debugLine="Sleep(duration)";
anywheresoftware.b4a.keywords.Common.Sleep(_ba,this,_duration);
this.state = 1;
return;
case 1:
//C
this.state = -1;
;
 //BA.debugLineNum = 193;BA.debugLine="p.RemoveViewFromParent";
_p.RemoveViewFromParent();
 //BA.debugLineNum = 194;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _vvvvvvvvv5(anywheresoftware.b4a.BA _ba) throws Exception{
 //BA.debugLineNum = 101;BA.debugLine="Sub GetDeviceId As String";
 //BA.debugLineNum = 121;BA.debugLine="Return FirebaseMessaging.fm.Token";
if (true) return mostCurrent._vvvvvvvvvvvvvvvvvvv1._vv6 /*anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper*/ .getToken();
 //BA.debugLineNum = 122;BA.debugLine="Log(FirebaseMessaging.fm.Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("540173589",mostCurrent._vvvvvvvvvvvvvvvvvvv1._vv6 /*anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper*/ .getToken(),0);
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvv6(anywheresoftware.b4a.BA _ba,String _titulo,String _imagen,String _text,String _textsub,Object _botontextoyes,Object _botontextocan,Object _botontextono,boolean _textolargo) throws Exception{
flm.b4a.betterdialogs.BetterDialogs _dial = null;
anywheresoftware.b4a.objects.ImageViewWrapper _msgbottomimg = null;
anywheresoftware.b4a.objects.PanelWrapper _panelcontent = null;
anywheresoftware.b4a.objects.LabelWrapper _titulolbl = null;
anywheresoftware.b4a.objects.ImageViewWrapper _msgtopimg = null;
anywheresoftware.b4a.objects.LabelWrapper _contenido = null;
anywheresoftware.b4a.objects.LabelWrapper _textosub = null;
String _msg = "";
anywheresoftware.b4a.objects.ImageViewWrapper _imgbitmap = null;
 //BA.debugLineNum = 20;BA.debugLine="Sub Mensaje (titulo As String, imagen As String, t";
 //BA.debugLineNum = 21;BA.debugLine="Dim dial As BetterDialogs";
_dial = new flm.b4a.betterdialogs.BetterDialogs();
 //BA.debugLineNum = 22;BA.debugLine="Dim MsgBottomImg As ImageView";
_msgbottomimg = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Dim panelcontent As Panel";
_panelcontent = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim titulolbl As Label";
_titulolbl = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Dim MsgTopImg As ImageView";
_msgtopimg = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim contenido As Label";
_contenido = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim textoSub As Label";
_textosub = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 30;BA.debugLine="MsgBottomImg.Initialize(\"\")";
_msgbottomimg.Initialize(_ba,"");
 //BA.debugLineNum = 31;BA.debugLine="MsgTopImg.Initialize(\"\")";
_msgtopimg.Initialize(_ba,"");
 //BA.debugLineNum = 32;BA.debugLine="panelcontent.Initialize(\"\")";
_panelcontent.Initialize(_ba,"");
 //BA.debugLineNum = 33;BA.debugLine="titulolbl.Initialize(\"\")";
_titulolbl.Initialize(_ba,"");
 //BA.debugLineNum = 34;BA.debugLine="contenido.Initialize(\"\")";
_contenido.Initialize(_ba,"");
 //BA.debugLineNum = 35;BA.debugLine="textoSub.Initialize(\"\")";
_textosub.Initialize(_ba,"");
 //BA.debugLineNum = 39;BA.debugLine="MsgTopImg.Gravity = Gravity.FILL";
_msgtopimg.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 40;BA.debugLine="MsgBottomImg.Gravity = Gravity.FILL";
_msgbottomimg.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 42;BA.debugLine="titulolbl.Text = titulo";
_titulolbl.setText(BA.ObjectToCharSequence(_titulo));
 //BA.debugLineNum = 43;BA.debugLine="titulolbl.TextColor = Colors.Black";
_titulolbl.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 44;BA.debugLine="titulolbl.Gravity = Gravity.CENTER_HORIZONTAL";
_titulolbl.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 45;BA.debugLine="titulolbl.TextSize = 22";
_titulolbl.setTextSize((float) (22));
 //BA.debugLineNum = 46;BA.debugLine="contenido.Text = text";
_contenido.setText(BA.ObjectToCharSequence(_text));
 //BA.debugLineNum = 47;BA.debugLine="contenido.TextColor = Colors.Black";
_contenido.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 48;BA.debugLine="contenido.TextSize = 18";
_contenido.setTextSize((float) (18));
 //BA.debugLineNum = 49;BA.debugLine="contenido.Gravity = Gravity.CENTER_HORIZONTAL";
_contenido.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 50;BA.debugLine="contenido.Color = Colors.white";
_contenido.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 51;BA.debugLine="textoSub.Text = textsub";
_textosub.setText(BA.ObjectToCharSequence(_textsub));
 //BA.debugLineNum = 52;BA.debugLine="textoSub.TextColor = Colors.DarkGray";
_textosub.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 53;BA.debugLine="textoSub.TextSize = 16";
_textosub.setTextSize((float) (16));
 //BA.debugLineNum = 54;BA.debugLine="textoSub.Gravity = Gravity.CENTER_HORIZONTAL";
_textosub.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 55;BA.debugLine="textoSub.Color = Colors.white";
_textosub.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 56;BA.debugLine="panelcontent.Color = Colors.white";
_panelcontent.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 59;BA.debugLine="Dim imgbitmap As ImageView";
_imgbitmap = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 60;BA.debugLine="imgbitmap.Initialize(\"\")";
_imgbitmap.Initialize(_ba,"");
 //BA.debugLineNum = 62;BA.debugLine="If imagen = Null Or imagen = \"Null\" Or imagen = \"";
if (_imagen== null || (_imagen).equals("Null") || (_imagen).equals("null")) { 
 //BA.debugLineNum = 63;BA.debugLine="imgbitmap.Bitmap =  LoadBitmapSample(File.DirAss";
_imgbitmap.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"MsgIcon.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)).getObject()));
 }else {
 //BA.debugLineNum = 65;BA.debugLine="imgbitmap.Bitmap =  LoadBitmapSample(File.DirAss";
_imgbitmap.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_imagen,anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),_ba)).getObject()));
 };
 //BA.debugLineNum = 74;BA.debugLine="panelcontent.AddView(MsgTopImg, 0, 0,90%x, 60dip)";
_panelcontent.AddView((android.view.View)(_msgtopimg.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),_ba),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 75;BA.debugLine="panelcontent.AddView(imgbitmap, 5%x, 3%y, 80%x, 3";
_panelcontent.AddView((android.view.View)(_imgbitmap.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),_ba),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (35),_ba));
 //BA.debugLineNum = 76;BA.debugLine="panelcontent.AddView(titulolbl, 0, 35%y, 90%x, 60";
_panelcontent.AddView((android.view.View)(_titulolbl.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (35),_ba),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),_ba),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 77;BA.debugLine="panelcontent.AddView(contenido, 5%x, titulolbl.To";
_panelcontent.AddView((android.view.View)(_contenido.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),_ba),(int) (_titulolbl.getTop()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),_ba)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),_ba));
 //BA.debugLineNum = 81;BA.debugLine="If textolargo = True Then";
if (_textolargo==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 82;BA.debugLine="panelcontent.AddView(textoSub, 5%x, contenido.To";
_panelcontent.AddView((android.view.View)(_textosub.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),_ba),(int) (_contenido.getTop()+_contenido.getHeight()+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),_ba));
 }else {
 //BA.debugLineNum = 84;BA.debugLine="panelcontent.AddView(textoSub, 5%x, contenido.T";
_panelcontent.AddView((android.view.View)(_textosub.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),_ba),(int) (_contenido.getTop()+_contenido.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (3),_ba)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),_ba));
 };
 //BA.debugLineNum = 87;BA.debugLine="panelcontent.AddView(MsgBottomImg, 0, 350dip, 90%";
_panelcontent.AddView((android.view.View)(_msgbottomimg.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (350)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),_ba),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 89;BA.debugLine="msg = dial.CustomDialog(\"\", 90%x, 60dip, panelcon";
_msg = BA.NumberToString(_dial.CustomDialog((Object)(""),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),_ba),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)),(anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(_panelcontent.getObject())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),_ba),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),_ba),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),_ba),anywheresoftware.b4a.keywords.Common.Null,_botontextoyes,_botontextocan,_botontextono,anywheresoftware.b4a.keywords.Common.False,"",_ba));
 //BA.debugLineNum = 90;BA.debugLine="Return msg";
if (true) return _msg;
 //BA.debugLineNum = 91;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvv7(anywheresoftware.b4a.BA _ba,boolean _activo,String _titulo,String _body) throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub MensajeNotificacion(activo As Boolean, titulo";
 //BA.debugLineNum = 133;BA.debugLine="MsgboxAsync(body,titulo)";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence(_body),BA.ObjectToCharSequence(_titulo),(_ba.processBA == null ? _ba : _ba.processBA));
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 5;BA.debugLine="Dim hc As OkHttpClient";
_vvvvvv1 = new anywheresoftware.b4h.okhttp.OkHttpClientWrapper();
 //BA.debugLineNum = 6;BA.debugLine="Dim out As OutputStream";
_vvvvvv2 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 7;BA.debugLine="Dim numfotosenviar As Int";
_vvvvvvvv0 = 0;
 //BA.debugLineNum = 9;BA.debugLine="Dim access As Accessiblity";
_vvvvvvvvv1 = new anywheresoftware.b4a.objects.Accessibility.Accessibility2();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvv0(anywheresoftware.b4a.BA _ba,anywheresoftware.b4a.objects.PanelWrapper _pan) throws Exception{
anywheresoftware.b4a.objects.ConcreteViewWrapper _v = null;
anywheresoftware.b4a.objects.LabelWrapper _lbl = null;
anywheresoftware.b4a.objects.SpinnerWrapper _s = null;
 //BA.debugLineNum = 205;BA.debugLine="Sub ResetUserFontScale(pan As Panel)";
 //BA.debugLineNum = 206;BA.debugLine="For Each v As View In pan";
_v = new anywheresoftware.b4a.objects.ConcreteViewWrapper();
{
final anywheresoftware.b4a.BA.IterableList group1 = _pan;
final int groupLen1 = group1.getSize()
;int index1 = 0;
;
for (; index1 < groupLen1;index1++){
_v = (anywheresoftware.b4a.objects.ConcreteViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.ConcreteViewWrapper(), (android.view.View)(group1.Get(index1)));
 //BA.debugLineNum = 207;BA.debugLine="If v Is Panel Then";
if (_v.getObjectOrNull() instanceof android.view.ViewGroup) { 
 //BA.debugLineNum = 208;BA.debugLine="ResetUserFontScale(v)";
_vvvvvvvvv0(_ba,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(_v.getObject())));
 }else if(_v.getObjectOrNull() instanceof android.widget.TextView) { 
 //BA.debugLineNum = 210;BA.debugLine="Dim lbl As Label = v";
_lbl = new anywheresoftware.b4a.objects.LabelWrapper();
_lbl = (anywheresoftware.b4a.objects.LabelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.LabelWrapper(), (android.widget.TextView)(_v.getObject()));
 //BA.debugLineNum = 211;BA.debugLine="lbl.TextSize = lbl.TextSize / access.GetUserFon";
_lbl.setTextSize((float) (_lbl.getTextSize()/(double)_vvvvvvvvv1.GetUserFontScale()));
 }else if(_v.getObjectOrNull() instanceof anywheresoftware.b4a.objects.SpinnerWrapper.B4ASpinner) { 
 //BA.debugLineNum = 213;BA.debugLine="Dim s As Spinner = v";
_s = new anywheresoftware.b4a.objects.SpinnerWrapper();
_s = (anywheresoftware.b4a.objects.SpinnerWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.SpinnerWrapper(), (anywheresoftware.b4a.objects.SpinnerWrapper.B4ASpinner)(_v.getObject()));
 //BA.debugLineNum = 214;BA.debugLine="s.TextSize = s.TextSize / access.GetUserFontSca";
_s.setTextSize((float) (_s.getTextSize()/(double)_vvvvvvvvv1.GetUserFontScale()));
 };
 }
};
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
}
