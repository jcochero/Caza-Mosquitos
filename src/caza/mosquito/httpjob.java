package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class httpjob extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "caza.mosquito.httpjob");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", caza.mosquito.httpjob.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
public boolean _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = false;
public String _vvvvvvvvvvvv7 = "";
public String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
public String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
public Object _vvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
public anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public Object _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
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
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 4;BA.debugLine="Public JobName As String";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 5;BA.debugLine="Public Success As Boolean";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = false;
 //BA.debugLineNum = 6;BA.debugLine="Public Username, Password As String";
_vvvvvvvvvvvv7 = "";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
 //BA.debugLineNum = 7;BA.debugLine="Public ErrorMessage As String";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 8;BA.debugLine="Private target As Object";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new Object();
 //BA.debugLineNum = 9;BA.debugLine="Private taskId As String";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 10;BA.debugLine="Private req As OkHttpRequest";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest();
 //BA.debugLineNum = 11;BA.debugLine="Public Tag As Object";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new Object();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvv0(int _id) throws Exception{
 //BA.debugLineNum = 97;BA.debugLine="Public Sub Complete (id As Int)";
 //BA.debugLineNum = 98;BA.debugLine="taskId = id";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = BA.NumberToString(_id);
 //BA.debugLineNum = 99;BA.debugLine="CallSubDelayed2(target, \"JobDone\", Me)";
__c.CallSubDelayed2(ba,_vvvvvvvvvvvvvvvvvvvvvvvvvvvv4,"JobDone",this);
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv1(String _link) throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Public Sub Download(Link As String)";
 //BA.debugLineNum = 68;BA.debugLine="req.InitializeGet(Link)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.InitializeGet(_link);
 //BA.debugLineNum = 69;BA.debugLine="CallSubDelayed2(HttpUtils2Service, \"SubmitJob\", M";
__c.CallSubDelayed2(ba,(Object)(_vvvvvvvvvvvvvvvvvvvvv3.getObject()),"SubmitJob",this);
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv2(String _link,String[] _parameters) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
anywheresoftware.b4a.objects.StringUtils _su = null;
int _i = 0;
 //BA.debugLineNum = 76;BA.debugLine="Public Sub Download2(Link As String, Parameters()";
 //BA.debugLineNum = 77;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 78;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 79;BA.debugLine="sb.Append(Link)";
_sb.Append(_link);
 //BA.debugLineNum = 80;BA.debugLine="If Parameters.Length > 0 Then sb.Append(\"?\")";
if (_parameters.length>0) { 
_sb.Append("?");};
 //BA.debugLineNum = 81;BA.debugLine="Dim su As StringUtils";
_su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 82;BA.debugLine="For i = 0 To Parameters.Length - 1 Step 2";
{
final int step6 = 2;
final int limit6 = (int) (_parameters.length-1);
_i = (int) (0) ;
for (;_i <= limit6 ;_i = _i + step6 ) {
 //BA.debugLineNum = 83;BA.debugLine="If i > 0 Then sb.Append(\"&\")";
if (_i>0) { 
_sb.Append("&");};
 //BA.debugLineNum = 84;BA.debugLine="sb.Append(su.EncodeUrl(Parameters(i), \"UTF8\")).A";
_sb.Append(_su.EncodeUrl(_parameters[_i],"UTF8")).Append("=");
 //BA.debugLineNum = 85;BA.debugLine="sb.Append(su.EncodeUrl(Parameters(i + 1), \"UTF8\"";
_sb.Append(_su.EncodeUrl(_parameters[(int) (_i+1)],"UTF8"));
 }
};
 //BA.debugLineNum = 87;BA.debugLine="req.InitializeGet(sb.ToString)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.InitializeGet(_sb.ToString());
 //BA.debugLineNum = 88;BA.debugLine="CallSubDelayed2(HttpUtils2Service, \"SubmitJob\", M";
__c.CallSubDelayed2(ba,(Object)(_vvvvvvvvvvvvvvvvvvvvv3.getObject()),"SubmitJob",this);
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _b = null;
 //BA.debugLineNum = 123;BA.debugLine="Public Sub GetBitmap As Bitmap";
 //BA.debugLineNum = 124;BA.debugLine="Dim b As Bitmap";
_b = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 125;BA.debugLine="b = LoadBitmap(HttpUtils2Service.TempFolder, task";
_b = __c.LoadBitmap(_vvvvvvvvvvvvvvvvvvvvv3._vvvvvvvv6 /*String*/ ,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7);
 //BA.debugLineNum = 126;BA.debugLine="Return b";
if (true) return _b;
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4a.objects.streams.File.InputStreamWrapper  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv4() throws Exception{
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
 //BA.debugLineNum = 129;BA.debugLine="Public Sub GetInputStream As InputStream";
 //BA.debugLineNum = 130;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 131;BA.debugLine="In = File.OpenInput(HttpUtils2Service.TempFolder,";
_in = __c.File.OpenInput(_vvvvvvvvvvvvvvvvvvvvv3._vvvvvvvv6 /*String*/ ,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7);
 //BA.debugLineNum = 132;BA.debugLine="Return In";
if (true) return _in;
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return null;
}
public anywheresoftware.b4h.okhttp.OkHttpClientWrapper.OkHttpRequest  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv5() throws Exception{
 //BA.debugLineNum = 92;BA.debugLine="Public Sub GetRequest As OkHttpRequest";
 //BA.debugLineNum = 93;BA.debugLine="Return req";
if (true) return _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0;
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return null;
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6() throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Public Sub GetString As String";
 //BA.debugLineNum = 109;BA.debugLine="Return GetString2(\"UTF8\")";
if (true) return _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv7("UTF8");
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv7(String _encoding) throws Exception{
anywheresoftware.b4a.objects.streams.File.TextReaderWrapper _tr = null;
String _res = "";
 //BA.debugLineNum = 113;BA.debugLine="Public Sub GetString2(Encoding As String) As Strin";
 //BA.debugLineNum = 114;BA.debugLine="Dim tr As TextReader";
_tr = new anywheresoftware.b4a.objects.streams.File.TextReaderWrapper();
 //BA.debugLineNum = 115;BA.debugLine="tr.Initialize2(File.OpenInput(HttpUtils2Service.T";
_tr.Initialize2((java.io.InputStream)(__c.File.OpenInput(_vvvvvvvvvvvvvvvvvvvvv3._vvvvvvvv6 /*String*/ ,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7).getObject()),_encoding);
 //BA.debugLineNum = 116;BA.debugLine="Dim res As String";
_res = "";
 //BA.debugLineNum = 117;BA.debugLine="res = tr.ReadAll";
_res = _tr.ReadAll();
 //BA.debugLineNum = 118;BA.debugLine="tr.Close";
_tr.Close();
 //BA.debugLineNum = 119;BA.debugLine="Return res";
if (true) return _res;
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public String  _initialize(anywheresoftware.b4a.BA _ba,String _name,Object _targetmodule) throws Exception{
innerInitialize(_ba);
 //BA.debugLineNum = 17;BA.debugLine="Public Sub Initialize (Name As String, TargetModul";
 //BA.debugLineNum = 18;BA.debugLine="JobName = Name";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = _name;
 //BA.debugLineNum = 19;BA.debugLine="target = TargetModule";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = _targetmodule;
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvv0(String _link,byte[] _data) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Public Sub PostBytes(Link As String, Data() As Byt";
 //BA.debugLineNum = 38;BA.debugLine="req.InitializePost2(Link, Data)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.InitializePost2(_link,_data);
 //BA.debugLineNum = 39;BA.debugLine="CallSubDelayed2(HttpUtils2Service, \"SubmitJob\", M";
__c.CallSubDelayed2(ba,(Object)(_vvvvvvvvvvvvvvvvvvvvv3.getObject()),"SubmitJob",this);
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1(String _link,String _dir,String _filename) throws Exception{
int _length = 0;
anywheresoftware.b4a.objects.streams.File.InputStreamWrapper _in = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
 //BA.debugLineNum = 44;BA.debugLine="Public Sub PostFile(Link As String, Dir As String,";
 //BA.debugLineNum = 45;BA.debugLine="Dim length As Int";
_length = 0;
 //BA.debugLineNum = 46;BA.debugLine="If Dir = File.DirAssets Then";
if ((_dir).equals(__c.File.getDirAssets())) { 
 //BA.debugLineNum = 47;BA.debugLine="Log(\"Cannot send files from the assets folder.\")";
__c.LogImpl("537093379","Cannot send files from the assets folder.",0);
 //BA.debugLineNum = 48;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 50;BA.debugLine="length = File.Size(Dir, FileName)";
_length = (int) (__c.File.Size(_dir,_filename));
 //BA.debugLineNum = 51;BA.debugLine="Dim In As InputStream";
_in = new anywheresoftware.b4a.objects.streams.File.InputStreamWrapper();
 //BA.debugLineNum = 52;BA.debugLine="In = File.OpenInput(Dir, FileName)";
_in = __c.File.OpenInput(_dir,_filename);
 //BA.debugLineNum = 53;BA.debugLine="If length < 1000000 Then '1mb";
if (_length<1000000) { 
 //BA.debugLineNum = 56;BA.debugLine="Dim out As OutputStream";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 57;BA.debugLine="out.InitializeToBytesArray(length)";
_out.InitializeToBytesArray(_length);
 //BA.debugLineNum = 58;BA.debugLine="File.Copy2(In, out)";
__c.File.Copy2((java.io.InputStream)(_in.getObject()),(java.io.OutputStream)(_out.getObject()));
 //BA.debugLineNum = 59;BA.debugLine="PostBytes(Link, out.ToBytesArray)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvv0(_link,_out.ToBytesArray());
 }else {
 //BA.debugLineNum = 61;BA.debugLine="req.InitializePost(Link, In, length)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.InitializePost(_link,(java.io.InputStream)(_in.getObject()),_length);
 //BA.debugLineNum = 62;BA.debugLine="CallSubDelayed2(HttpUtils2Service, \"SubmitJob\",";
__c.CallSubDelayed2(ba,(Object)(_vvvvvvvvvvvvvvvvvvvvv3.getObject()),"SubmitJob",this);
 };
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2(String _link,String _text) throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Public Sub PostString(Link As String, Text As Stri";
 //BA.debugLineNum = 23;BA.debugLine="PostBytes(Link, Text.GetBytes(\"UTF8\"))";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvv0(_link,_text.getBytes("UTF8"));
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public String  _vvvvvvvvvvvvvvvvvvvvvvvvv1() throws Exception{
 //BA.debugLineNum = 103;BA.debugLine="Public Sub Release";
 //BA.debugLineNum = 104;BA.debugLine="File.Delete(HttpUtils2Service.TempFolder, taskId)";
__c.File.Delete(_vvvvvvvvvvvvvvvvvvvvv3._vvvvvvvv6 /*String*/ ,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7);
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
