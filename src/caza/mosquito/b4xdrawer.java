package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class b4xdrawer extends B4AClass.ImplB4AClass implements BA.SubDelegator{
    private static java.util.HashMap<String, java.lang.reflect.Method> htSubs;
    private void innerInitialize(BA _ba) throws Exception {
        if (ba == null) {
            ba = new BA(_ba, this, htSubs, "caza.mosquito.b4xdrawer");
            if (htSubs == null) {
                ba.loadHtSubs(this.getClass());
                htSubs = ba.htSubs;
            }
            
        }
        if (BA.isShellModeRuntimeCheck(ba)) 
			   this.getClass().getMethod("_class_globals", caza.mosquito.b4xdrawer.class).invoke(this, new Object[] {null});
        else
            ba.raiseEvent2(null, true, "class_globals", false);
    }

 public anywheresoftware.b4a.keywords.Common __c = null;
public String _vvvvvvvvvvvvvvvv3 = "";
public Object _vvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper.XUI _vvvvvvvvvvvvvvvv5 = null;
public int _vvvvvvvvvvvvvvvv6 = 0;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.B4XViewWrapper _vvvvvvvvvvvvvvvvv2 = null;
public int _vvvvvvvvvvvvvvvvv3 = 0;
public float _vvvvvvvvvvvvvvvvv4 = 0f;
public float _vvvvvvvvvvvvvvvvv5 = 0f;
public boolean _vvvvvvvvvvvvvvvvv6 = false;
public boolean _vvvvvvvvvvvvvvvvv7 = false;
public boolean _vvvvvvvvvvvvvvvvv0 = false;
public boolean _vvvvvvvvvvvvvvvvvv1 = false;
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
public boolean  _base_onintercepttouchevent(int _action,float _x,float _y,Object _motionevent) throws Exception{
float _dx = 0f;
float _dy = 0f;
 //BA.debugLineNum = 93;BA.debugLine="Private Sub Base_OnInterceptTouchEvent (Action As";
 //BA.debugLineNum = 94;BA.debugLine="If mEnabled = False Then Return False";
if (_vvvvvvvvvvvvvvvvvv1==__c.False) { 
if (true) return __c.False;};
 //BA.debugLineNum = 95;BA.debugLine="If IsOpen = False And x > mLeftPanel.Left + mLeft";
if (_vvvvvvvvvvvvvvvvv6==__c.False && _x>_vvvvvvvvvvvvvvvv7.getLeft()+_vvvvvvvvvvvvvvvv7.getWidth()+_vvvvvvvvvvvvvvvvv3) { 
if (true) return __c.False;};
 //BA.debugLineNum = 96;BA.debugLine="If IsOpen And x > mLeftPanel.Left + mLeftPanel.Wi";
if (_vvvvvvvvvvvvvvvvv6 && _x>_vvvvvvvvvvvvvvvv7.getLeft()+_vvvvvvvvvvvvvvvv7.getWidth()) { 
 //BA.debugLineNum = 98;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 100;BA.debugLine="If HandlingSwipe Then Return True";
if (_vvvvvvvvvvvvvvvvv7) { 
if (true) return __c.True;};
 //BA.debugLineNum = 101;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,_vvvvvvvvvvvvvvvvv1.TOUCH_ACTION_DOWN,_vvvvvvvvvvvvvvvvv1.TOUCH_ACTION_MOVE)) {
case 0: {
 //BA.debugLineNum = 103;BA.debugLine="TouchXStart = X";
_vvvvvvvvvvvvvvvvv4 = _x;
 //BA.debugLineNum = 104;BA.debugLine="TouchYStart = Y";
_vvvvvvvvvvvvvvvvv5 = _y;
 //BA.debugLineNum = 105;BA.debugLine="HandlingSwipe = False";
_vvvvvvvvvvvvvvvvv7 = __c.False;
 break; }
case 1: {
 //BA.debugLineNum = 107;BA.debugLine="Dim dx As Float = Abs(x - TouchXStart)";
_dx = (float) (__c.Abs(_x-_vvvvvvvvvvvvvvvvv4));
 //BA.debugLineNum = 108;BA.debugLine="Dim dy As Float = Abs(y - TouchYStart)";
_dy = (float) (__c.Abs(_y-_vvvvvvvvvvvvvvvvv5));
 //BA.debugLineNum = 109;BA.debugLine="If dy < 20dip And dx > 10dip Then";
if (_dy<__c.DipToCurrent((int) (20)) && _dx>__c.DipToCurrent((int) (10))) { 
 //BA.debugLineNum = 110;BA.debugLine="HandlingSwipe = True";
_vvvvvvvvvvvvvvvvv7 = __c.True;
 };
 break; }
}
;
 //BA.debugLineNum = 113;BA.debugLine="Return HandlingSwipe";
if (true) return _vvvvvvvvvvvvvvvvv7;
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return false;
}
public boolean  _base_ontouchevent(int _action,float _x,float _y,Object _motionevent) throws Exception{
int _leftpanelrightside = 0;
float _dx = 0f;
 //BA.debugLineNum = 62;BA.debugLine="Private Sub Base_OnTouchEvent (Action As Int, X As";
 //BA.debugLineNum = 63;BA.debugLine="If mEnabled = False Then Return False";
if (_vvvvvvvvvvvvvvvvvv1==__c.False) { 
if (true) return __c.False;};
 //BA.debugLineNum = 64;BA.debugLine="Dim LeftPanelRightSide As Int = mLeftPanel.Left +";
_leftpanelrightside = (int) (_vvvvvvvvvvvvvvvv7.getLeft()+_vvvvvvvvvvvvvvvv7.getWidth());
 //BA.debugLineNum = 65;BA.debugLine="If HandlingSwipe = False And x > LeftPanelRightSi";
if (_vvvvvvvvvvvvvvvvv7==__c.False && _x>_leftpanelrightside) { 
 //BA.debugLineNum = 66;BA.debugLine="If IsOpen Then";
if (_vvvvvvvvvvvvvvvvv6) { 
 //BA.debugLineNum = 67;BA.debugLine="TouchXStart = X";
_vvvvvvvvvvvvvvvvv4 = _x;
 //BA.debugLineNum = 68;BA.debugLine="If Action = mBasePanel.TOUCH_ACTION_UP Then set";
if (_action==_vvvvvvvvvvvvvvvvv1.TOUCH_ACTION_UP) { 
_setvvvvvvvvvvvvvvvv1(__c.False);};
 //BA.debugLineNum = 69;BA.debugLine="Return True";
if (true) return __c.True;
 };
 //BA.debugLineNum = 71;BA.debugLine="If IsOpen = False And x > LeftPanelRightSide + E";
if (_vvvvvvvvvvvvvvvvv6==__c.False && _x>_leftpanelrightside+_vvvvvvvvvvvvvvvvv3) { 
 //BA.debugLineNum = 72;BA.debugLine="Return False";
if (true) return __c.False;
 };
 };
 //BA.debugLineNum = 75;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,_vvvvvvvvvvvvvvvvv1.TOUCH_ACTION_MOVE,_vvvvvvvvvvvvvvvvv1.TOUCH_ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 77;BA.debugLine="Dim dx As Float = x - TouchXStart";
_dx = (float) (_x-_vvvvvvvvvvvvvvvvv4);
 //BA.debugLineNum = 78;BA.debugLine="TouchXStart = X";
_vvvvvvvvvvvvvvvvv4 = _x;
 //BA.debugLineNum = 79;BA.debugLine="If HandlingSwipe Or Abs(dx) > 3dip Then";
if (_vvvvvvvvvvvvvvvvv7 || __c.Abs(_dx)>__c.DipToCurrent((int) (3))) { 
 //BA.debugLineNum = 80;BA.debugLine="HandlingSwipe = True";
_vvvvvvvvvvvvvvvvv7 = __c.True;
 //BA.debugLineNum = 81;BA.debugLine="ChangeOffset(mLeftPanel.Left + dx, True, False";
_vvvvvvvvvvvvvv6((float) (_vvvvvvvvvvvvvvvv7.getLeft()+_dx),__c.True,__c.False);
 };
 break; }
case 1: {
 //BA.debugLineNum = 84;BA.debugLine="If HandlingSwipe Then";
if (_vvvvvvvvvvvvvvvvv7) { 
 //BA.debugLineNum = 85;BA.debugLine="ChangeOffset(mLeftPanel.Left, False, False)";
_vvvvvvvvvvvvvv6((float) (_vvvvvvvvvvvvvvvv7.getLeft()),__c.False,__c.False);
 };
 //BA.debugLineNum = 87;BA.debugLine="HandlingSwipe = False";
_vvvvvvvvvvvvvvvvv7 = __c.False;
 break; }
}
;
 //BA.debugLineNum = 89;BA.debugLine="Return True";
if (true) return __c.True;
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return false;
}
public String  _vvvvvvvvvvvvvv6(float _x,boolean _currentlytouching,boolean _noanimation) throws Exception{
int _visibleoffset = 0;
int _dx = 0;
int _duration = 0;
 //BA.debugLineNum = 170;BA.debugLine="Private Sub ChangeOffset (x As Float, CurrentlyTou";
 //BA.debugLineNum = 171;BA.debugLine="x = Max(-mSideWidth, Min(0, x))";
_x = (float) (__c.Max(-_vvvvvvvvvvvvvvvv6,__c.Min(0,_x)));
 //BA.debugLineNum = 172;BA.debugLine="Dim VisibleOffset As Int = mSideWidth + x";
_visibleoffset = (int) (_vvvvvvvvvvvvvvvv6+_x);
 //BA.debugLineNum = 181;BA.debugLine="If CurrentlyTouching = False Then";
if (_currentlytouching==__c.False) { 
 //BA.debugLineNum = 182;BA.debugLine="If (IsOpen And VisibleOffset < 0.8 * mSideWidth)";
if ((_vvvvvvvvvvvvvvvvv6 && _visibleoffset<0.8*_vvvvvvvvvvvvvvvv6) || (_vvvvvvvvvvvvvvvvv6==__c.False && _visibleoffset<0.2*_vvvvvvvvvvvvvvvv6)) { 
 //BA.debugLineNum = 183;BA.debugLine="x = -mSideWidth";
_x = (float) (-_vvvvvvvvvvvvvvvv6);
 //BA.debugLineNum = 184;BA.debugLine="IsOpen = False";
_vvvvvvvvvvvvvvvvv6 = __c.False;
 }else {
 //BA.debugLineNum = 186;BA.debugLine="x = 0";
_x = (float) (0);
 //BA.debugLineNum = 187;BA.debugLine="IsOpen = True";
_vvvvvvvvvvvvvvvvv6 = __c.True;
 };
 //BA.debugLineNum = 189;BA.debugLine="Dim dx As Int = Abs(mLeftPanel.Left - x)";
_dx = (int) (__c.Abs(_vvvvvvvvvvvvvvvv7.getLeft()-_x));
 //BA.debugLineNum = 190;BA.debugLine="Dim duration As Int = Max(0, 200 * dx / mSideWid";
_duration = (int) (__c.Max(0,200*_dx/(double)_vvvvvvvvvvvvvvvv6));
 //BA.debugLineNum = 191;BA.debugLine="If NoAnimation Then duration = 0";
if (_noanimation) { 
_duration = (int) (0);};
 //BA.debugLineNum = 192;BA.debugLine="mLeftPanel.SetLayoutAnimated(duration, x, 0, mLe";
_vvvvvvvvvvvvvvvv7.SetLayoutAnimated(_duration,(int) (_x),(int) (0),_vvvvvvvvvvvvvvvv7.getWidth(),_vvvvvvvvvvvvvvvv7.getHeight());
 //BA.debugLineNum = 193;BA.debugLine="mDarkPanel.SetColorAnimated(duration, mDarkPanel";
_vvvvvvvvvvvvvvvv0.SetColorAnimated(_duration,_vvvvvvvvvvvvvvvv0.getColor(),_vvvvvvvvvvvvvvv3((int) (_x)));
 }else {
 //BA.debugLineNum = 201;BA.debugLine="mDarkPanel.Color = OffsetToColor(x)";
_vvvvvvvvvvvvvvvv0.setColor(_vvvvvvvvvvvvvvv3((int) (_x)));
 //BA.debugLineNum = 202;BA.debugLine="mLeftPanel.Left = x";
_vvvvvvvvvvvvvvvv7.setLeft((int) (_x));
 };
 //BA.debugLineNum = 205;BA.debugLine="End Sub";
return "";
}
public String  _class_globals() throws Exception{
 //BA.debugLineNum = 2;BA.debugLine="Sub Class_Globals";
 //BA.debugLineNum = 3;BA.debugLine="Private mEventName As String 'ignore";
_vvvvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 4;BA.debugLine="Private mCallBack As Object 'ignore";
_vvvvvvvvvvvvvvvv4 = new Object();
 //BA.debugLineNum = 5;BA.debugLine="Private xui As XUI 'ignore";
_vvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.B4XViewWrapper.XUI();
 //BA.debugLineNum = 6;BA.debugLine="Private mSideWidth As Int";
_vvvvvvvvvvvvvvvv6 = 0;
 //BA.debugLineNum = 7;BA.debugLine="Private mLeftPanel As B4XView";
_vvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 8;BA.debugLine="Private mDarkPanel As B4XView";
_vvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Private mBasePanel As B4XView";
_vvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Private mCenterPanel As B4XView";
_vvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.B4XViewWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Private ExtraWidth As Int = 50dip";
_vvvvvvvvvvvvvvvvv3 = __c.DipToCurrent((int) (50));
 //BA.debugLineNum = 12;BA.debugLine="Private TouchXStart, TouchYStart As Float 'ignore";
_vvvvvvvvvvvvvvvvv4 = 0f;
_vvvvvvvvvvvvvvvvv5 = 0f;
 //BA.debugLineNum = 13;BA.debugLine="Private IsOpen As Boolean";
_vvvvvvvvvvvvvvvvv6 = false;
 //BA.debugLineNum = 14;BA.debugLine="Private HandlingSwipe As Boolean";
_vvvvvvvvvvvvvvvvv7 = false;
 //BA.debugLineNum = 15;BA.debugLine="Private StartAtScrim As Boolean 'ignore";
_vvvvvvvvvvvvvvvvv0 = false;
 //BA.debugLineNum = 16;BA.debugLine="Private mEnabled As Boolean = True";
_vvvvvvvvvvvvvvvvvv1 = __c.True;
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getvvvvvvvvvvvvvvv7() throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Public Sub getCenterPanel As B4XView";
 //BA.debugLineNum = 228;BA.debugLine="Return mCenterPanel";
if (true) return _vvvvvvvvvvvvvvvvv2;
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return null;
}
public boolean  _getvvvvvvvvvvvvvvv0() throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Public Sub getGestureEnabled As Boolean";
 //BA.debugLineNum = 240;BA.debugLine="Return mEnabled";
if (true) return _vvvvvvvvvvvvvvvvvv1;
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return false;
}
public boolean  _getvvvvvvvvvvvvvvvv1() throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Public Sub getLeftOpen As Boolean";
 //BA.debugLineNum = 213;BA.debugLine="Return IsOpen";
if (true) return _vvvvvvvvvvvvvvvvv6;
 //BA.debugLineNum = 214;BA.debugLine="End Sub";
return false;
}
public anywheresoftware.b4a.objects.B4XViewWrapper  _getvvvvvvvvvvvvvvvv2() throws Exception{
 //BA.debugLineNum = 223;BA.debugLine="Public Sub getLeftPanel As B4XView";
 //BA.debugLineNum = 224;BA.debugLine="Return mLeftPanel";
if (true) return _vvvvvvvvvvvvvvvv7;
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return null;
}
public String  _initialize(anywheresoftware.b4a.BA _ba,Object _callback,String _eventname,anywheresoftware.b4a.objects.B4XViewWrapper _parent,int _sidewidth) throws Exception{
innerInitialize(_ba);
anywheresoftware.b4a.objects.TouchPanelCreator _creator = null;
anywheresoftware.b4a.objects.PanelWrapper _p = null;
 //BA.debugLineNum = 19;BA.debugLine="Public Sub Initialize (Callback As Object, EventNa";
 //BA.debugLineNum = 20;BA.debugLine="mEventName = EventName";
_vvvvvvvvvvvvvvvv3 = _eventname;
 //BA.debugLineNum = 21;BA.debugLine="mCallBack = Callback";
_vvvvvvvvvvvvvvvv4 = _callback;
 //BA.debugLineNum = 22;BA.debugLine="mSideWidth = SideWidth";
_vvvvvvvvvvvvvvvv6 = _sidewidth;
 //BA.debugLineNum = 24;BA.debugLine="Dim creator As TouchPanelCreator";
_creator = new anywheresoftware.b4a.objects.TouchPanelCreator();
 //BA.debugLineNum = 25;BA.debugLine="mBasePanel = creator.CreateTouchPanel(\"base\")";
_vvvvvvvvvvvvvvvvv1 = (anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(_creator.CreateTouchPanel(ba,"base").getObject()));
 //BA.debugLineNum = 32;BA.debugLine="Parent.AddView(mBasePanel, 0, 0, Parent.Width, Pa";
_parent.AddView((android.view.View)(_vvvvvvvvvvvvvvvvv1.getObject()),(int) (0),(int) (0),_parent.getWidth(),_parent.getHeight());
 //BA.debugLineNum = 33;BA.debugLine="mCenterPanel = xui.CreatePanel(\"\")";
_vvvvvvvvvvvvvvvvv2 = _vvvvvvvvvvvvvvvv5.CreatePanel(ba,"");
 //BA.debugLineNum = 34;BA.debugLine="mBasePanel.AddView(mCenterPanel, 0, 0, mBasePanel";
_vvvvvvvvvvvvvvvvv1.AddView((android.view.View)(_vvvvvvvvvvvvvvvvv2.getObject()),(int) (0),(int) (0),_vvvvvvvvvvvvvvvvv1.getWidth(),_vvvvvvvvvvvvvvvvv1.getHeight());
 //BA.debugLineNum = 35;BA.debugLine="mDarkPanel = xui.CreatePanel(\"dark\")";
_vvvvvvvvvvvvvvvv0 = _vvvvvvvvvvvvvvvv5.CreatePanel(ba,"dark");
 //BA.debugLineNum = 36;BA.debugLine="mBasePanel.AddView(mDarkPanel, 0, 0, mBasePanel.W";
_vvvvvvvvvvvvvvvvv1.AddView((android.view.View)(_vvvvvvvvvvvvvvvv0.getObject()),(int) (0),(int) (0),_vvvvvvvvvvvvvvvvv1.getWidth(),_vvvvvvvvvvvvvvvvv1.getHeight());
 //BA.debugLineNum = 37;BA.debugLine="mLeftPanel = xui.CreatePanel(\"LeftPanel\")";
_vvvvvvvvvvvvvvvv7 = _vvvvvvvvvvvvvvvv5.CreatePanel(ba,"LeftPanel");
 //BA.debugLineNum = 38;BA.debugLine="mBasePanel.AddView(mLeftPanel, -SideWidth, 0, Sid";
_vvvvvvvvvvvvvvvvv1.AddView((android.view.View)(_vvvvvvvvvvvvvvvv7.getObject()),(int) (-_sidewidth),(int) (0),_sidewidth,_vvvvvvvvvvvvvvvvv1.getHeight());
 //BA.debugLineNum = 39;BA.debugLine="mLeftPanel.Color = xui.Color_Red";
_vvvvvvvvvvvvvvvv7.setColor(_vvvvvvvvvvvvvvvv5.Color_Red);
 //BA.debugLineNum = 41;BA.debugLine="Dim p As Panel = mLeftPanel";
_p = new anywheresoftware.b4a.objects.PanelWrapper();
_p = (anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(_vvvvvvvvvvvvvvvv7.getObject()));
 //BA.debugLineNum = 42;BA.debugLine="p.Elevation = 4dip";
_p.setElevation((float) (__c.DipToCurrent((int) (4))));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public String  _leftpanel_click() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Private Sub LeftPanel_Click";
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public int  _vvvvvvvvvvvvvvv3(int _x) throws Exception{
float _visible = 0f;
 //BA.debugLineNum = 207;BA.debugLine="Private Sub OffsetToColor (x As Int) As Int";
 //BA.debugLineNum = 208;BA.debugLine="Dim Visible As Float = (mSideWidth + x) / mSideWi";
_visible = (float) ((_vvvvvvvvvvvvvvvv6+_x)/(double)_vvvvvvvvvvvvvvvv6);
 //BA.debugLineNum = 209;BA.debugLine="Return xui.Color_ARGB(100 * Visible, 0, 0, 0)";
if (true) return _vvvvvvvvvvvvvvvv5.Color_ARGB((int) (100*_visible),(int) (0),(int) (0),(int) (0));
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return 0;
}
public String  _vvvvvvvvvvvvvvv4(int _width,int _height) throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Public Sub Resize(Width As Int, Height As Int)";
 //BA.debugLineNum = 232;BA.debugLine="If IsOpen Then ChangeOffset(-mSideWidth, False, T";
if (_vvvvvvvvvvvvvvvvv6) { 
_vvvvvvvvvvvvvv6((float) (-_vvvvvvvvvvvvvvvv6),__c.False,__c.True);};
 //BA.debugLineNum = 233;BA.debugLine="mBasePanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_vvvvvvvvvvvvvvvvv1.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 234;BA.debugLine="mLeftPanel.SetLayoutAnimated(0, mLeftPanel.Left,";
_vvvvvvvvvvvvvvvv7.SetLayoutAnimated((int) (0),_vvvvvvvvvvvvvvvv7.getLeft(),(int) (0),_vvvvvvvvvvvvvvvv7.getWidth(),_vvvvvvvvvvvvvvvvv1.getHeight());
 //BA.debugLineNum = 235;BA.debugLine="mDarkPanel.SetLayoutAnimated(0, 0, 0, Width, Heig";
_vvvvvvvvvvvvvvvv0.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 236;BA.debugLine="mCenterPanel.SetLayoutAnimated(0, 0, 0, Width, He";
_vvvvvvvvvvvvvvvvv2.SetLayoutAnimated((int) (0),(int) (0),(int) (0),_width,_height);
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public String  _setvvvvvvvvvvvvvvv0(boolean _b) throws Exception{
 //BA.debugLineNum = 243;BA.debugLine="Public Sub setGestureEnabled (b As Boolean)";
 //BA.debugLineNum = 244;BA.debugLine="mEnabled = b";
_vvvvvvvvvvvvvvvvvv1 = _b;
 //BA.debugLineNum = 245;BA.debugLine="End Sub";
return "";
}
public String  _setvvvvvvvvvvvvvvvv1(boolean _b) throws Exception{
float _x = 0f;
 //BA.debugLineNum = 216;BA.debugLine="Public Sub setLeftOpen (b As Boolean)";
 //BA.debugLineNum = 217;BA.debugLine="If b = IsOpen Then Return";
if (_b==_vvvvvvvvvvvvvvvvv6) { 
if (true) return "";};
 //BA.debugLineNum = 218;BA.debugLine="Dim x As Float";
_x = 0f;
 //BA.debugLineNum = 219;BA.debugLine="If b Then x = 0 Else x = -mSideWidth";
if (_b) { 
_x = (float) (0);}
else {
_x = (float) (-_vvvvvvvvvvvvvvvv6);};
 //BA.debugLineNum = 220;BA.debugLine="ChangeOffset(x, False, False)";
_vvvvvvvvvvvvvv6(_x,__c.False,__c.False);
 //BA.debugLineNum = 221;BA.debugLine="End Sub";
return "";
}
public Object callSub(String sub, Object sender, Object[] args) throws Exception {
BA.senderHolder.set(sender);
return BA.SubDelegator.SubNotFound;
}
}
