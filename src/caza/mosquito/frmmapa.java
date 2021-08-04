package caza.mosquito;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class frmmapa extends Activity implements B4AActivity{
	public static frmmapa mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmmapa");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmmapa).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmmapa");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmmapa", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmmapa) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmmapa) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return frmmapa.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (frmmapa) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmmapa) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            frmmapa mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmmapa) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static uk.co.martinpearman.b4a.fusedlocationprovider.FusedLocationProviderWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public static anywheresoftware.b4a.gps.LocationWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public static anywheresoftware.b4a.objects.RuntimePermissions _v6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btndetectar = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllon = null;
public uk.co.martinpearman.b4a.osmdroid.views.MapView _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnzoomall = null;
public uk.co.martinpearman.b4a.osmdroid.views.overlay.SimpleLocationOverlay _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmapa = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public uk.co.martinpearman.b4a.osmdroid.views.overlay.MarkerOverlay _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public uk.co.martinpearman.b4a.osmdroid.Constants _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public uk.co.martinpearman.b4a.osmdroid.views.overlay.ViewHost _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlconfig = null;
public anywheresoftware.b4a.objects.ScrollViewWrapper _scrconfig = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnokconfig = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnconfigexplorar = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = null;
public anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkdatospropios = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkdatosusuarios = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncheckall = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblespecies = null;
public anywheresoftware.b4a.phone.Phone _vvvvvvv6 = null;
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
public caza.mosquito.frmpoliticadatos _vvvvvvvvvvvvvvvvvvvvv1 = null;
public caza.mosquito.frmquehacer _vvvvvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.httputils2service _vvvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.multipartpost _vvvvvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.register _vvvvvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.uploadfiles _vvvvvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.utilidades _vvvvvvvvvvvvvvvvvvvvv7 = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 61;BA.debugLine="rp.CheckAndRequest(rp.PERMISSION_ACCESS_FINE_LOCA";
_v6.CheckAndRequest(processBA,_v6.PERMISSION_ACCESS_FINE_LOCATION);
 //BA.debugLineNum = 62;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 63;BA.debugLine="FusedLocationProvider1.Initialize(\"FusedLocation";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(processBA,"FusedLocationProvider1");
 };
 //BA.debugLineNum = 66;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._vvvvvvv6.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 67;BA.debugLine="Activity.LoadLayout(\"frmprincipal_Explorar\")";
mostCurrent._activity.LoadLayout("frmprincipal_Explorar",mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 79;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 80;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 81;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 82;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 84;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 74;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _activity_permissionresult(String _permission,boolean _result) throws Exception{
 //BA.debugLineNum = 96;BA.debugLine="Sub Activity_PermissionResult (Permission As Strin";
 //BA.debugLineNum = 97;BA.debugLine="If Permission = rp.PERMISSION_ACCESS_FINE_LOCATIO";
if ((_permission).equals(_v6.PERMISSION_ACCESS_FINE_LOCATION)) { 
 //BA.debugLineNum = 98;BA.debugLine="CargarMapa";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 };
 //BA.debugLineNum = 100;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _btncheckall_click() throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chk = null;
 //BA.debugLineNum = 625;BA.debugLine="Sub btnCheckAll_Click";
 //BA.debugLineNum = 627;BA.debugLine="If btnCheckAll.Text = \"Seleccionar todas\" Or btnC";
if ((mostCurrent._btncheckall.getText()).equals("Seleccionar todas") || (mostCurrent._btncheckall.getText()).equals("Select all")) { 
 //BA.debugLineNum = 628;BA.debugLine="For i = 0 To listaConfig.Size - 1";
{
final int step2 = 1;
final int limit2 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit2 ;_i = _i + step2 ) {
 //BA.debugLineNum = 629;BA.debugLine="Dim chk As CheckBox";
_chk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 630;BA.debugLine="chk = listaConfig.Get(i)";
_chk = (anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper(), (android.widget.CheckBox)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Get(_i)));
 //BA.debugLineNum = 631;BA.debugLine="chk.Checked = True";
_chk.setChecked(anywheresoftware.b4a.keywords.Common.True);
 }
};
 //BA.debugLineNum = 633;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 634;BA.debugLine="btnCheckAll.Text = \"Deseleccionar todas\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Deseleccionar todas"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 636;BA.debugLine="btnCheckAll.Text = \"Deselect all\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Deselect all"));
 };
 }else {
 //BA.debugLineNum = 642;BA.debugLine="For i = 0 To listaConfig.Size - 1";
{
final int step13 = 1;
final int limit13 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit13 ;_i = _i + step13 ) {
 //BA.debugLineNum = 643;BA.debugLine="Dim chk As CheckBox";
_chk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 644;BA.debugLine="chk = listaConfig.Get(i)";
_chk = (anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper(), (android.widget.CheckBox)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Get(_i)));
 //BA.debugLineNum = 645;BA.debugLine="chk.Checked = False";
_chk.setChecked(anywheresoftware.b4a.keywords.Common.False);
 }
};
 //BA.debugLineNum = 647;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 648;BA.debugLine="btnCheckAll.Text = \"Seleccionar todas\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Seleccionar todas"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 650;BA.debugLine="btnCheckAll.Text = \"Select all\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Select all"));
 };
 };
 //BA.debugLineNum = 654;BA.debugLine="End Sub";
return "";
}
public static String  _btnconfigexplorar_click() throws Exception{
 //BA.debugLineNum = 538;BA.debugLine="Sub btnConfigExplorar_Click";
 //BA.debugLineNum = 539;BA.debugLine="Activity.LoadLayout(\"frmprincipal_Explorar_Config";
mostCurrent._activity.LoadLayout("frmprincipal_Explorar_Config",mostCurrent.activityBA);
 //BA.debugLineNum = 541;BA.debugLine="btnCheckAll.Visible = True";
mostCurrent._btncheckall.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 542;BA.debugLine="btnZoomAll.Visible = False";
mostCurrent._btnzoomall.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 543;BA.debugLine="btnConfigExplorar.Visible = False";
mostCurrent._btnconfigexplorar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 544;BA.debugLine="btnDetectar.Visible = False";
mostCurrent._btndetectar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 545;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 546;BA.debugLine="btnConfigExplorar.Text = \" Filtrar\"";
mostCurrent._btnconfigexplorar.setText(BA.ObjectToCharSequence(" Filtrar"));
 //BA.debugLineNum = 547;BA.debugLine="chkDatosPropios.Text = \"Datos propios\"";
mostCurrent._chkdatospropios.setText(BA.ObjectToCharSequence("Datos propios"));
 //BA.debugLineNum = 548;BA.debugLine="chkDatosUsuarios.Text = \"Datos de otros usuarios";
mostCurrent._chkdatosusuarios.setText(BA.ObjectToCharSequence("Datos de otros usuarios"));
 //BA.debugLineNum = 549;BA.debugLine="btnCheckAll.Text = \"Deseleccionar todas\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Deseleccionar todas"));
 //BA.debugLineNum = 550;BA.debugLine="lblEspecies.Text = \"Especies:\"";
mostCurrent._lblespecies.setText(BA.ObjectToCharSequence("Especies:"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 552;BA.debugLine="btnConfigExplorar.Text = \" Filter\"";
mostCurrent._btnconfigexplorar.setText(BA.ObjectToCharSequence(" Filter"));
 //BA.debugLineNum = 553;BA.debugLine="chkDatosPropios.Text = \"Own data\"";
mostCurrent._chkdatospropios.setText(BA.ObjectToCharSequence("Own data"));
 //BA.debugLineNum = 554;BA.debugLine="chkDatosUsuarios.Text = \"Other user data\"";
mostCurrent._chkdatosusuarios.setText(BA.ObjectToCharSequence("Other user data"));
 //BA.debugLineNum = 555;BA.debugLine="lblEspecies.Text = \"Species:\"";
mostCurrent._lblespecies.setText(BA.ObjectToCharSequence("Species:"));
 //BA.debugLineNum = 556;BA.debugLine="btnCheckAll.Text = \"Deselect all\"";
mostCurrent._btncheckall.setText(BA.ObjectToCharSequence("Deselect all"));
 };
 //BA.debugLineNum = 558;BA.debugLine="CargarConfigExplorar";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7();
 //BA.debugLineNum = 559;BA.debugLine="End Sub";
return "";
}
public static String  _btndetectar_click() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Sub btnDetectar_Click";
 //BA.debugLineNum = 144;BA.debugLine="DetectarPosicion";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7();
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _btnokconfig_click() throws Exception{
anywheresoftware.b4a.objects.collections.List _listmostrar = null;
int _i = 0;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chk = null;
String _spname = "";
anywheresoftware.b4a.objects.collections.List _listmostrarconfig = null;
uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker _markeramostrar = null;
String _snippet = "";
String _tipoeval = "";
String _fotopath = "";
int _j = 0;
 //BA.debugLineNum = 655;BA.debugLine="Sub btnOkConfig_Click";
 //BA.debugLineNum = 659;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 660;BA.debugLine="ProgressDialogShow(\"Actualizando mapa...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Actualizando mapa..."));
 //BA.debugLineNum = 661;BA.debugLine="ToastMessageShow(\"Actualizando mapa...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Actualizando mapa..."),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 663;BA.debugLine="ProgressDialogShow(\"Updating map...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Updating map..."));
 //BA.debugLineNum = 664;BA.debugLine="ToastMessageShow(\"Updating mapa...\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Updating mapa..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 667;BA.debugLine="Dim listMostrar As List";
_listmostrar = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 668;BA.debugLine="listMostrar.Initialize";
_listmostrar.Initialize();
 //BA.debugLineNum = 669;BA.debugLine="listaChequeados.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize();
 //BA.debugLineNum = 671;BA.debugLine="For i = 0 To listaConfig.Size - 1";
{
final int step11 = 1;
final int limit11 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit11 ;_i = _i + step11 ) {
 //BA.debugLineNum = 672;BA.debugLine="Dim chk As CheckBox";
_chk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 673;BA.debugLine="chk = listaConfig.Get(i)";
_chk = (anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper(), (android.widget.CheckBox)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Get(_i)));
 //BA.debugLineNum = 674;BA.debugLine="listaChequeados.Add(chk)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Add((Object)(_chk.getObject()));
 //BA.debugLineNum = 675;BA.debugLine="If chk.Checked = True Then";
if (_chk.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 677;BA.debugLine="If chk.Text = \"Criaderos\" Then";
if ((_chk.getText()).equals("Criaderos")) { 
 //BA.debugLineNum = 678;BA.debugLine="listMostrar.Add(\"Criaderos\")";
_listmostrar.Add((Object)("Criaderos"));
 }else {
 //BA.debugLineNum = 681;BA.debugLine="Dim spname As String";
_spname = "";
 //BA.debugLineNum = 682;BA.debugLine="spname = chk.Text.SubString2(chk.Text.LastInde";
_spname = _chk.getText().substring(_chk.getText().lastIndexOf(" "),_chk.getText().length());
 //BA.debugLineNum = 683;BA.debugLine="If spname.EndsWith(\"*\") Then";
if (_spname.endsWith("*")) { 
 //BA.debugLineNum = 684;BA.debugLine="spname = spname.SubString2(0, spname.Length -";
_spname = _spname.substring((int) (0),(int) (_spname.length()-1));
 };
 //BA.debugLineNum = 687;BA.debugLine="listMostrar.Add(spname.Trim)";
_listmostrar.Add((Object)(_spname.trim()));
 };
 };
 }
};
 //BA.debugLineNum = 694;BA.debugLine="Dim listMostrarConfig As List";
_listmostrarconfig = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 695;BA.debugLine="listMostrarConfig.Initialize";
_listmostrarconfig.Initialize();
 //BA.debugLineNum = 697;BA.debugLine="If chkDatosPropios.Checked = True Then";
if (mostCurrent._chkdatospropios.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 698;BA.debugLine="listMostrarConfig.Add(\"usuario\")";
_listmostrarconfig.Add((Object)("usuario"));
 }else {
 //BA.debugLineNum = 700;BA.debugLine="listMostrarConfig.Add(\"no\")";
_listmostrarconfig.Add((Object)("no"));
 };
 //BA.debugLineNum = 702;BA.debugLine="If chkDatosUsuarios.Checked = True Then";
if (mostCurrent._chkdatosusuarios.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 703;BA.debugLine="listMostrarConfig.Add(\"otrosusuarios\")";
_listmostrarconfig.Add((Object)("otrosusuarios"));
 }else {
 //BA.debugLineNum = 705;BA.debugLine="listMostrarConfig.Add(\"no\")";
_listmostrarconfig.Add((Object)("no"));
 };
 //BA.debugLineNum = 708;BA.debugLine="pnlConfig.RemoveView";
mostCurrent._pnlconfig.RemoveView();
 //BA.debugLineNum = 709;BA.debugLine="btnConfigExplorar.Visible =  True";
mostCurrent._btnconfigexplorar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 710;BA.debugLine="btnCheckAll.Visible = False";
mostCurrent._btncheckall.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 711;BA.debugLine="btnZoomAll.Visible = True";
mostCurrent._btnzoomall.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 712;BA.debugLine="btnDetectar.Visible = True";
mostCurrent._btndetectar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 716;BA.debugLine="markersOverlay.RemoveAllItems";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.RemoveAllItems();
 //BA.debugLineNum = 717;BA.debugLine="MapView1.GetOverlays.Remove(markersOverlay)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Remove((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()));
 //BA.debugLineNum = 727;BA.debugLine="For i = 0 To markersList.Size - 1";
{
final int step47 = 1;
final int limit47 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit47 ;_i = _i + step47 ) {
 //BA.debugLineNum = 728;BA.debugLine="Dim markerAMostrar As OSMDroid_Marker";
_markeramostrar = new uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker();
 //BA.debugLineNum = 729;BA.debugLine="markerAMostrar = markersList.Get(i)";
_markeramostrar = (uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker(), (uk.co.martinpearman.osmdroid.views.overlay.Marker)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Get(_i)));
 //BA.debugLineNum = 730;BA.debugLine="Dim snippet As String = markerAMostrar.GetSnippe";
_snippet = _markeramostrar.GetSnippet().substring((int) (0),_markeramostrar.GetSnippet().indexOf("//"));
 //BA.debugLineNum = 731;BA.debugLine="Dim tipoeval As String  = markerAMostrar.GetSnip";
_tipoeval = _markeramostrar.GetSnippet().substring((int) (_markeramostrar.GetSnippet().indexOf("//")+2),_markeramostrar.GetSnippet().indexOf("////"));
 //BA.debugLineNum = 732;BA.debugLine="Dim fotopath As String  = markerAMostrar.GetSnip";
_fotopath = _markeramostrar.GetSnippet().substring((int) (_markeramostrar.GetSnippet().indexOf("////")+4),_markeramostrar.GetSnippet().length());
 //BA.debugLineNum = 736;BA.debugLine="If tipoeval = \"Criadero\" Then";
if ((_tipoeval).equals("Criadero")) { 
 //BA.debugLineNum = 737;BA.debugLine="If listMostrar.Size = 6 Then";
if (_listmostrar.getSize()==6) { 
 //BA.debugLineNum = 738;BA.debugLine="If snippet = Main.username And listMostrarConf";
if ((_snippet).equals(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ) && (_listmostrarconfig.Get((int) (0))).equals((Object)("usuario"))) { 
 //BA.debugLineNum = 739;BA.debugLine="markersOverlay.AddItem(markerAMostrar)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.AddItem((uk.co.martinpearman.osmdroid.views.overlay.Marker)(_markeramostrar.getObject()));
 }else if((_snippet).equals(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ) == false && (_listmostrarconfig.Get((int) (1))).equals((Object)("otrosusuarios"))) { 
 //BA.debugLineNum = 741;BA.debugLine="markersOverlay.AddItem(markerAMostrar)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.AddItem((uk.co.martinpearman.osmdroid.views.overlay.Marker)(_markeramostrar.getObject()));
 };
 };
 }else {
 //BA.debugLineNum = 746;BA.debugLine="If snippet = Main.username And listMostrarConfi";
if ((_snippet).equals(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ) && (_listmostrarconfig.Get((int) (0))).equals((Object)("usuario"))) { 
 //BA.debugLineNum = 747;BA.debugLine="Dim j As Int = listMostrar.IndexOf(markerAMost";
_j = _listmostrar.IndexOf((Object)(_markeramostrar.GetTitle()));
 //BA.debugLineNum = 748;BA.debugLine="If j > -1 Then";
if (_j>-1) { 
 //BA.debugLineNum = 749;BA.debugLine="markersOverlay.AddItem(markerAMostrar)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.AddItem((uk.co.martinpearman.osmdroid.views.overlay.Marker)(_markeramostrar.getObject()));
 };
 }else if((_snippet).equals(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ) == false && (_listmostrarconfig.Get((int) (1))).equals((Object)("otrosusuarios"))) { 
 //BA.debugLineNum = 752;BA.debugLine="Dim j As Int = listMostrar.IndexOf(markerAMost";
_j = _listmostrar.IndexOf((Object)(_markeramostrar.GetTitle()));
 //BA.debugLineNum = 753;BA.debugLine="If j > -1 Then";
if (_j>-1) { 
 //BA.debugLineNum = 754;BA.debugLine="markersOverlay.AddItem(markerAMostrar)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.AddItem((uk.co.martinpearman.osmdroid.views.overlay.Marker)(_markeramostrar.getObject()));
 };
 };
 };
 }
};
 //BA.debugLineNum = 760;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 761;BA.debugLine="ToastMessageShow(\"Se encontraron \" & (markersOve";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Se encontraron "+BA.NumberToString((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Size()))+" observaciones"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 763;BA.debugLine="ToastMessageShow(\"We found \" & (markersOverlay.S";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("We found "+BA.NumberToString((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Size()))+" reports"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 767;BA.debugLine="MapView1.GetOverlays.Add(markersOverlay)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()));
 //BA.debugLineNum = 768;BA.debugLine="MapView1.Invalidate";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Invalidate();
 //BA.debugLineNum = 769;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 770;BA.debugLine="End Sub";
return "";
}
public static String  _btnzoomall_click() throws Exception{
 //BA.debugLineNum = 502;BA.debugLine="Sub btnZoomAll_Click";
 //BA.debugLineNum = 503;BA.debugLine="MapView1.GetController.SetZoom(5)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (5));
 //BA.debugLineNum = 504;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7() throws Exception{
int _i = 0;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chklista = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chknew = null;
anywheresoftware.b4a.objects.ImageViewWrapper _chkimg = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chk = null;
String[] _currentsp = null;
anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _criaderoschk = null;
 //BA.debugLineNum = 560;BA.debugLine="Sub CargarConfigExplorar";
 //BA.debugLineNum = 565;BA.debugLine="If listaConfig.IsInitialized = False Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 566;BA.debugLine="listaConfig.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize();
 }else {
 //BA.debugLineNum = 569;BA.debugLine="listaConfig.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize();
 //BA.debugLineNum = 570;BA.debugLine="For i=0 To listaChequeados.Size - 1";
{
final int step5 = 1;
final int limit5 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.getSize()-1);
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 571;BA.debugLine="Dim chklista As CheckBox";
_chklista = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 572;BA.debugLine="Dim chkNew As CheckBox";
_chknew = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 573;BA.debugLine="chklista = listaChequeados.Get(i)";
_chklista = (anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper(), (android.widget.CheckBox)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Get(_i)));
 //BA.debugLineNum = 574;BA.debugLine="chkNew.Initialize(\"\")";
_chknew.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 575;BA.debugLine="chkNew.Text = chklista.Text";
_chknew.setText(BA.ObjectToCharSequence(_chklista.getText()));
 //BA.debugLineNum = 576;BA.debugLine="chkNew.TextColor = Colors.Black";
_chknew.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 577;BA.debugLine="chkNew.Checked = chklista.Checked";
_chknew.setChecked(_chklista.getChecked());
 //BA.debugLineNum = 578;BA.debugLine="listaConfig.Add(chkNew)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Add((Object)(_chknew.getObject()));
 //BA.debugLineNum = 579;BA.debugLine="scrConfig.Panel.AddView(chkNew,0,55dip * (i-1)";
mostCurrent._scrconfig.getPanel().AddView((android.view.View)(_chknew.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55))*(_i-1)+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 580;BA.debugLine="scrConfig.Color = Colors.White";
mostCurrent._scrconfig.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 583;BA.debugLine="Dim chkImg As ImageView";
_chkimg = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 584;BA.debugLine="chkImg.Initialize(\"\")";
_chkimg.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 585;BA.debugLine="If chkNew.Text = \"Aedes aegypti\" Then";
if ((_chknew.getText()).equals("Aedes aegypti")) { 
 //BA.debugLineNum = 586;BA.debugLine="chkImg.Bitmap = LoadBitmapSample(File.DirAsset";
_chkimg.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker1.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else if((_chknew.getText()).equals("Aedes albifasciatus")) { 
 //BA.debugLineNum = 588;BA.debugLine="chkImg.Bitmap = LoadBitmapSample(File.DirAsset";
_chkimg.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker2.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else if((_chknew.getText()).equals("Aedes albopictus")) { 
 //BA.debugLineNum = 590;BA.debugLine="chkImg.Bitmap = LoadBitmapSample(File.DirAsset";
_chkimg.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker3.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else if((_chknew.getText()).equals("Culex sp.")) { 
 //BA.debugLineNum = 592;BA.debugLine="chkImg.Bitmap = LoadBitmapSample(File.DirAsset";
_chkimg.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker4.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 }else if((_chknew.getText()).equals("Criadero")) { 
 //BA.debugLineNum = 594;BA.debugLine="chkImg.Bitmap = LoadBitmapSample(File.DirAsset";
_chkimg.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"markerCriadero.png",anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)).getObject()));
 };
 //BA.debugLineNum = 596;BA.debugLine="scrConfig.Panel.AddView(chkImg, 90%x, 55dip * (";
mostCurrent._scrconfig.getPanel().AddView((android.view.View)(_chkimg.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (55))*(_i-1)+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 }
};
 //BA.debugLineNum = 600;BA.debugLine="scrConfig.Panel.Height = Main.speciesMap.Size *";
mostCurrent._scrconfig.getPanel().setHeight((int) (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv4 /*anywheresoftware.b4a.objects.collections.List*/ .getSize()*anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 601;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 604;BA.debugLine="For i=0 To Main.speciesMap.Size - 1";
{
final int step34 = 1;
final int limit34 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv4 /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1);
_i = (int) (0) ;
for (;_i <= limit34 ;_i = _i + step34 ) {
 //BA.debugLineNum = 605;BA.debugLine="listaChequeados.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0.Initialize();
 //BA.debugLineNum = 606;BA.debugLine="Dim chk As CheckBox";
_chk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 607;BA.debugLine="chk.Initialize(\"\")";
_chk.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 608;BA.debugLine="Dim currentsp() As String";
_currentsp = new String[(int) (0)];
java.util.Arrays.fill(_currentsp,"");
 //BA.debugLineNum = 609;BA.debugLine="currentsp = Main.speciesMap.Get(i)";
_currentsp = (String[])(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv4 /*anywheresoftware.b4a.objects.collections.List*/ .Get(_i));
 //BA.debugLineNum = 610;BA.debugLine="chk.Text = currentsp(1)";
_chk.setText(BA.ObjectToCharSequence(_currentsp[(int) (1)]));
 //BA.debugLineNum = 611;BA.debugLine="chk.TextColor = Colors.DarkGray";
_chk.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 612;BA.debugLine="chk.Checked = True";
_chk.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 613;BA.debugLine="listaConfig.Add(chk)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Add((Object)(_chk.getObject()));
 //BA.debugLineNum = 614;BA.debugLine="scrConfig.Panel.AddView(chk,0,50dip * (i-1), Act";
mostCurrent._scrconfig.getPanel().AddView((android.view.View)(_chk.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))*(_i-1)),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 }
};
 //BA.debugLineNum = 616;BA.debugLine="Dim criaderosChk As CheckBox";
_criaderoschk = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 617;BA.debugLine="criaderosChk.Initialize(\"\")";
_criaderoschk.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 618;BA.debugLine="criaderosChk.Text = \"Criaderos\"";
_criaderoschk.setText(BA.ObjectToCharSequence("Criaderos"));
 //BA.debugLineNum = 619;BA.debugLine="criaderosChk.TextColor = Colors.DarkGray";
_criaderoschk.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 620;BA.debugLine="criaderosChk.Checked = True";
_criaderoschk.setChecked(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 621;BA.debugLine="listaConfig.Add(criaderosChk)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Add((Object)(_criaderoschk.getObject()));
 //BA.debugLineNum = 622;BA.debugLine="scrConfig.Panel.AddView(criaderosChk,0,50dip * (M";
mostCurrent._scrconfig.getPanel().AddView((android.view.View)(_criaderoschk.getObject()),(int) (0),(int) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))*(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv4 /*anywheresoftware.b4a.objects.collections.List*/ .getSize()-1)),mostCurrent._activity.getWidth(),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 623;BA.debugLine="scrConfig.Panel.Height = Main.speciesMap.Size * 5";
mostCurrent._scrconfig.getPanel().setHeight((int) (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv4 /*anywheresoftware.b4a.objects.collections.List*/ .getSize()*anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 624;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0() throws Exception{
uk.co.martinpearman.b4a.osmdroid.util.GeoPoint _geopoint1 = null;
 //BA.debugLineNum = 110;BA.debugLine="Sub CargarMapa";
 //BA.debugLineNum = 112;BA.debugLine="If LastLocation.IsInitialized Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.IsInitialized()) { 
 //BA.debugLineNum = 113;BA.debugLine="lblLat.Text = LastLocation.Latitude";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()));
 //BA.debugLineNum = 114;BA.debugLine="lblLon.Text = LastLocation.Longitude";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()));
 }else {
 //BA.debugLineNum = 117;BA.debugLine="lblLat.Text = \"-34.9204950\"";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence("-34.9204950"));
 //BA.debugLineNum = 118;BA.debugLine="lblLon.Text = \"-57.9535660\"";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence("-57.9535660"));
 };
 //BA.debugLineNum = 120;BA.debugLine="MapView1.Initialize(\"MapView1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"MapView1");
 //BA.debugLineNum = 121;BA.debugLine="MapView1.SetBuiltInZoomControls(True)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.SetBuiltInZoomControls(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="MapView1.SetMultiTouchControls(True)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.SetMultiTouchControls(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 124;BA.debugLine="Dim geopoint1 As OSMDroid_GeoPoint";
_geopoint1 = new uk.co.martinpearman.b4a.osmdroid.util.GeoPoint();
 //BA.debugLineNum = 125;BA.debugLine="geopoint1.Initialize(lblLat.Text,lblLon.Text)";
_geopoint1.Initialize((double)(Double.parseDouble(mostCurrent._lbllat.getText())),(double)(Double.parseDouble(mostCurrent._lbllon.getText())));
 //BA.debugLineNum = 126;BA.debugLine="MapView1.GetController.SetCenter(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetCenter((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="MapView1.GetController.SetZoom(6)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (6));
 //BA.debugLineNum = 129;BA.debugLine="pnlMapa.RemoveAllViews";
mostCurrent._pnlmapa.RemoveAllViews();
 //BA.debugLineNum = 130;BA.debugLine="pnlMapa.AddView(MapView1, 0,0, 100%x, 100%y)";
mostCurrent._pnlmapa.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 131;BA.debugLine="If SimpleLocationOverlay1.IsInitialized = False T";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 132;BA.debugLine="SimpleLocationOverlay1.Initialize()";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(processBA);
 //BA.debugLineNum = 133;BA.debugLine="MapView1.GetOverlays.Add(SimpleLocationOverlay1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()));
 //BA.debugLineNum = 134;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 }else {
 //BA.debugLineNum = 136;BA.debugLine="MapView1.GetOverlays.Add(SimpleLocationOverlay1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()));
 //BA.debugLineNum = 137;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 };
 //BA.debugLineNum = 139;BA.debugLine="GetMiMapa";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 140;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7() throws Exception{
 //BA.debugLineNum = 148;BA.debugLine="Sub DetectarPosicion";
 //BA.debugLineNum = 151;BA.debugLine="fondoblanco.Initialize(\"fondoblanco\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(mostCurrent.activityBA,"fondoblanco");
 //BA.debugLineNum = 152;BA.debugLine="detectandoLabel.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 153;BA.debugLine="detectandoLabel.TextColor = Colors.White";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 154;BA.debugLine="detectandoLabel.Gravity = Gravity.CENTER_HORIZONT";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 156;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 157;BA.debugLine="detectandoLabel.Text = \"Buscando tu posición aut";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Buscando tu posición automáticamente..."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 159;BA.debugLine="detectandoLabel.Text = \"Looking for your positio";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Looking for your position automatically..."));
 };
 //BA.debugLineNum = 163;BA.debugLine="fondoblanco.Color = Colors.ARGB(150, 64,64,64)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (64),(int) (64),(int) (64)));
 //BA.debugLineNum = 164;BA.debugLine="Activity.AddView(fondoblanco, 0,0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 165;BA.debugLine="Activity.AddView(detectandoLabel, 5%x, 55%y, 80%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (55),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 166;BA.debugLine="btnDetectar.Enabled = False";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 167;BA.debugLine="btnDetectar.Color = Colors.Black";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 168;BA.debugLine="btnDetectar.TextColor = Colors.White";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 170;BA.debugLine="If FusedLocationProvider1.IsInitialized = False T";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 172;BA.debugLine="FusedLocationProvider1.Initialize(\"FusedLocation";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(processBA,"FusedLocationProviderExplorar");
 //BA.debugLineNum = 173;BA.debugLine="Log(\"init fusedlocationproviderExplorar\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534406425","init fusedlocationproviderExplorar",0);
 };
 //BA.debugLineNum = 175;BA.debugLine="If FusedLocationProvider1.IsConnected = False The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 176;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 //BA.debugLineNum = 177;BA.debugLine="Log(\"connecting fusedlocationproviderExplorar\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534406429","connecting fusedlocationproviderExplorar",0);
 };
 //BA.debugLineNum = 179;BA.debugLine="End Sub";
return "";
}
public static String  _fondoblanco_click() throws Exception{
 //BA.debugLineNum = 183;BA.debugLine="Sub fondoblanco_Click";
 //BA.debugLineNum = 185;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 186;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 187;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="fondoblanco.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.RemoveView();
 //BA.debugLineNum = 190;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 //BA.debugLineNum = 191;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionfailed(int _connectionresult1) throws Exception{
 //BA.debugLineNum = 194;BA.debugLine="Sub FusedLocationProvider1_ConnectionFailed(Connec";
 //BA.debugLineNum = 195;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionFailed\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534537473","FusedLocationProvider1_ConnectionFailed",0);
 //BA.debugLineNum = 199;BA.debugLine="Select ConnectionResult1";
switch (BA.switchObjectToInt(_connectionresult1,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.ConnectionResult.NETWORK_ERROR)) {
case 0: {
 //BA.debugLineNum = 203;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 break; }
default: {
 break; }
}
;
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionsuccess() throws Exception{
uk.co.martinpearman.b4a.fusedlocationprovider.LocationRequest _locationrequest1 = null;
uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsRequestBuilder _locationsettingsrequestbuilder1 = null;
 //BA.debugLineNum = 208;BA.debugLine="Sub FusedLocationProvider1_ConnectionSuccess";
 //BA.debugLineNum = 209;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionSuccess\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534603009","FusedLocationProvider1_ConnectionSuccess",0);
 //BA.debugLineNum = 210;BA.debugLine="Dim LocationRequest1 As LocationRequest";
_locationrequest1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationRequest();
 //BA.debugLineNum = 211;BA.debugLine="LocationRequest1.Initialize";
_locationrequest1.Initialize();
 //BA.debugLineNum = 212;BA.debugLine="LocationRequest1.SetInterval(1000)	'	1000 millise";
_locationrequest1.SetInterval((long) (1000));
 //BA.debugLineNum = 214;BA.debugLine="LocationRequest1.SetPriority(LocationRequest1.Pri";
_locationrequest1.SetPriority(_locationrequest1.Priority.PRIORITY_HIGH_ACCURACY);
 //BA.debugLineNum = 215;BA.debugLine="LocationRequest1.SetSmallestDisplacement(1)	'	1 m";
_locationrequest1.SetSmallestDisplacement((float) (1));
 //BA.debugLineNum = 217;BA.debugLine="Dim LocationSettingsRequestBuilder1 As LocationSe";
_locationsettingsrequestbuilder1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsRequestBuilder();
 //BA.debugLineNum = 218;BA.debugLine="LocationSettingsRequestBuilder1.Initialize";
_locationsettingsrequestbuilder1.Initialize();
 //BA.debugLineNum = 219;BA.debugLine="LocationSettingsRequestBuilder1.AddLocationReques";
_locationsettingsrequestbuilder1.AddLocationRequest((com.google.android.gms.location.LocationRequest)(_locationrequest1.getObject()));
 //BA.debugLineNum = 220;BA.debugLine="FusedLocationProvider1.CheckLocationSettings(Loca";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.CheckLocationSettings(_locationsettingsrequestbuilder1.Build());
 //BA.debugLineNum = 222;BA.debugLine="FusedLocationProvider1.RequestLocationUpdates(Loc";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.RequestLocationUpdates((com.google.android.gms.location.LocationRequest)(_locationrequest1.getObject()));
 //BA.debugLineNum = 224;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 225;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 226;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 227;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionsuspended(int _suspendedcause1) throws Exception{
 //BA.debugLineNum = 228;BA.debugLine="Sub FusedLocationProvider1_ConnectionSuspended(Sus";
 //BA.debugLineNum = 229;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionSuspended\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534668545","FusedLocationProvider1_ConnectionSuspended",0);
 //BA.debugLineNum = 233;BA.debugLine="Select SuspendedCause1";
switch (BA.switchObjectToInt(_suspendedcause1,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SuspendedCause.CAUSE_NETWORK_LOST,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SuspendedCause.CAUSE_SERVICE_DISCONNECTED)) {
case 0: {
 break; }
case 1: {
 break; }
}
;
 //BA.debugLineNum = 239;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
 //BA.debugLineNum = 240;BA.debugLine="Sub FusedLocationProvider1_LocationChanged(Locatio";
 //BA.debugLineNum = 241;BA.debugLine="Log(\"FusedLocationProvider1_LocationChanged\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534734081","FusedLocationProvider1_LocationChanged",0);
 //BA.debugLineNum = 242;BA.debugLine="LastLocation=Location1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = _location1;
 //BA.debugLineNum = 243;BA.debugLine="Log(LastLocation.Latitude)";
anywheresoftware.b4a.keywords.Common.LogImpl("534734083",BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()),0);
 //BA.debugLineNum = 244;BA.debugLine="Log(LastLocation.Longitude)";
anywheresoftware.b4a.keywords.Common.LogImpl("534734084",BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()),0);
 //BA.debugLineNum = 245;BA.debugLine="UpdateUI";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_locationsettingschecked(uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsResult _locationsettingsresult1) throws Exception{
uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsStatus _locationsettingsstatus1 = null;
 //BA.debugLineNum = 247;BA.debugLine="Sub FusedLocationProvider1_LocationSettingsChecked";
 //BA.debugLineNum = 248;BA.debugLine="Log(\"FusedLocationProvider1_LocationSettingsCheck";
anywheresoftware.b4a.keywords.Common.LogImpl("534799617","FusedLocationProvider1_LocationSettingsChecked",0);
 //BA.debugLineNum = 249;BA.debugLine="Dim LocationSettingsStatus1 As LocationSettingsSt";
_locationsettingsstatus1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsStatus();
_locationsettingsstatus1 = _locationsettingsresult1.GetLocationSettingsStatus();
 //BA.debugLineNum = 250;BA.debugLine="Select LocationSettingsStatus1.GetStatusCode";
switch (BA.switchObjectToInt(_locationsettingsstatus1.GetStatusCode(),_locationsettingsstatus1.StatusCodes.RESOLUTION_REQUIRED,_locationsettingsstatus1.StatusCodes.SETTINGS_CHANGE_UNAVAILABLE,_locationsettingsstatus1.StatusCodes.SUCCESS)) {
case 0: {
 //BA.debugLineNum = 252;BA.debugLine="Log(\"RESOLUTION_REQUIRED\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534799621","RESOLUTION_REQUIRED",0);
 //BA.debugLineNum = 255;BA.debugLine="LocationSettingsStatus1.StartResolutionDialog(\"";
_locationsettingsstatus1.StartResolutionDialog(mostCurrent.activityBA,"LocationSettingsResult1");
 break; }
case 1: {
 //BA.debugLineNum = 257;BA.debugLine="Log(\"SETTINGS_CHANGE_UNAVAILABLE\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534799626","SETTINGS_CHANGE_UNAVAILABLE",0);
 //BA.debugLineNum = 260;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 261;BA.debugLine="MsgBoxAsync(\"Error, tu dispositivo no tiene lo";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Error, tu dispositivo no tiene localización. Busca tu posición en el mapa!"),BA.ObjectToCharSequence("Problem"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 263;BA.debugLine="MsgboxAsync(\"Error, your device cannot be loca";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Error, your device cannot be located. Find your location in the map!"),BA.ObjectToCharSequence("Problem"),processBA);
 };
 //BA.debugLineNum = 265;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 break; }
case 2: {
 //BA.debugLineNum = 267;BA.debugLine="Log(\"SUCCESS\")";
anywheresoftware.b4a.keywords.Common.LogImpl("534799636","SUCCESS",0);
 break; }
}
;
 //BA.debugLineNum = 271;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 341;BA.debugLine="Sub GetMiMapa";
 //BA.debugLineNum = 343;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 344;BA.debugLine="ProgressDialogShow(\"Buscando puntos cercanos...\"";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Buscando puntos cercanos..."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 346;BA.debugLine="ProgressDialogShow(\"Getting nearby reports...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Getting nearby reports..."));
 };
 //BA.debugLineNum = 351;BA.debugLine="btnDetectar.Enabled = False";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 355;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 356;BA.debugLine="dd.url = Main.serverPath & \"/connect2/getallmapa.";
_dd.url /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/getallmapa.php";
 //BA.debugLineNum = 357;BA.debugLine="dd.EventName = \"GetMiMapa\"";
_dd.EventName /*String*/  = "GetMiMapa";
 //BA.debugLineNum = 358;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmmapa.getObject();
 //BA.debugLineNum = 359;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 361;BA.debugLine="End Sub";
return "";
}
public static String  _getmimapa_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
String _numresults = "";
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _markercriadero = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _marker1 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _marker2 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _marker3 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _marker4 = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _markercriaderodr = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _marker1dr = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _marker2dr = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _marker3dr = null;
anywheresoftware.b4a.objects.drawable.BitmapDrawable _marker4dr = null;
int _i = 0;
anywheresoftware.b4a.objects.collections.Map _newpunto = null;
double _sitiolat = 0;
double _sitiolong = 0;
String _tipoeval = "";
String _valormosquito = "";
String _nombreusuario = "";
String _foto1pathmarker = "";
uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker _marker = null;
String _markersnippet = "";
uk.co.martinpearman.b4a.osmdroid.views.overlay.ViewHostOverlay _viewhostoverlay1 = null;
 //BA.debugLineNum = 362;BA.debugLine="Sub GetMiMapa_Complete(Job As HttpJob)";
 //BA.debugLineNum = 363;BA.debugLine="Log(\"GetMapa messages: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("535061761","GetMapa messages: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 364;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 365;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 366;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 367;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 368;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 369;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 370;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 371;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 372;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 373;BA.debugLine="ToastMessageShow(\"No encuentro sitios anterior";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No encuentro sitios anteriores, prueba luego"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 375;BA.debugLine="ToastMessageShow(\"Cannot find previous reports";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Cannot find previous reports, try again later"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else if((_act).equals("Error")) { 
 //BA.debugLineNum = 378;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 379;BA.debugLine="ToastMessageShow(\"No encuentro sitios anterior";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No encuentro sitios anteriores, prueba luego"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 381;BA.debugLine="ToastMessageShow(\"Cannot find previous reports";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Cannot find previous reports, try again later"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else if((_act).equals("GetMapaOk")) { 
 //BA.debugLineNum = 386;BA.debugLine="Dim numresults As String";
_numresults = "";
 //BA.debugLineNum = 387;BA.debugLine="numresults = parser.NextValue";
_numresults = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 393;BA.debugLine="markersOverlay.Initialize(\"markersOverlay\", Map";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize(processBA,"markersOverlay",(org.osmdroid.views.MapView)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()));
 //BA.debugLineNum = 394;BA.debugLine="MapView1.GetOverlays.Add(markersOverlay)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getObject()));
 //BA.debugLineNum = 397;BA.debugLine="markersList.Initialize()";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize();
 //BA.debugLineNum = 401;BA.debugLine="Dim markerCriadero As Bitmap";
_markercriadero = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 402;BA.debugLine="Dim marker1 As Bitmap";
_marker1 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 403;BA.debugLine="Dim marker2 As Bitmap";
_marker2 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 404;BA.debugLine="Dim marker3 As Bitmap";
_marker3 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 405;BA.debugLine="Dim marker4 As Bitmap";
_marker4 = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 406;BA.debugLine="markerCriadero.Initialize(File.DirAssets, \"mark";
_markercriadero.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"markerCriadero.png");
 //BA.debugLineNum = 407;BA.debugLine="marker1.Initialize(File.DirAssets, \"marker1.png";
_marker1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker1.png");
 //BA.debugLineNum = 408;BA.debugLine="marker2.Initialize(File.DirAssets, \"marker2.png";
_marker2.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker2.png");
 //BA.debugLineNum = 409;BA.debugLine="marker3.Initialize(File.DirAssets, \"marker3.png";
_marker3.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker3.png");
 //BA.debugLineNum = 410;BA.debugLine="marker4.Initialize(File.DirAssets, \"marker4.png";
_marker4.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"marker4.png");
 //BA.debugLineNum = 411;BA.debugLine="Dim markerCriaderodr As BitmapDrawable";
_markercriaderodr = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 412;BA.debugLine="Dim marker1dr As BitmapDrawable";
_marker1dr = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 413;BA.debugLine="Dim marker2dr As BitmapDrawable";
_marker2dr = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 414;BA.debugLine="Dim marker3dr As BitmapDrawable";
_marker3dr = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 415;BA.debugLine="Dim marker4dr As BitmapDrawable";
_marker4dr = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 417;BA.debugLine="markerCriaderodr.Initialize(markerCriadero)";
_markercriaderodr.Initialize((android.graphics.Bitmap)(_markercriadero.getObject()));
 //BA.debugLineNum = 418;BA.debugLine="marker1dr.Initialize(marker1)";
_marker1dr.Initialize((android.graphics.Bitmap)(_marker1.getObject()));
 //BA.debugLineNum = 419;BA.debugLine="marker2dr.Initialize(marker2)";
_marker2dr.Initialize((android.graphics.Bitmap)(_marker2.getObject()));
 //BA.debugLineNum = 420;BA.debugLine="marker3dr.Initialize(marker3)";
_marker3dr.Initialize((android.graphics.Bitmap)(_marker3.getObject()));
 //BA.debugLineNum = 421;BA.debugLine="marker4dr.Initialize(marker4)";
_marker4dr.Initialize((android.graphics.Bitmap)(_marker4.getObject()));
 //BA.debugLineNum = 429;BA.debugLine="For i = 0 To numresults - 1";
{
final int step47 = 1;
final int limit47 = (int) ((double)(Double.parseDouble(_numresults))-1);
_i = (int) (0) ;
for (;_i <= limit47 ;_i = _i + step47 ) {
 //BA.debugLineNum = 433;BA.debugLine="Dim newpunto As Map";
_newpunto = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 434;BA.debugLine="newpunto = parser.NextObject";
_newpunto = _parser.NextObject();
 //BA.debugLineNum = 435;BA.debugLine="Dim sitiolat As Double = newpunto.Get(\"lat\")";
_sitiolat = (double)(BA.ObjectToNumber(_newpunto.Get((Object)("lat"))));
 //BA.debugLineNum = 436;BA.debugLine="Dim sitiolong As Double = newpunto.Get(\"lng\")";
_sitiolong = (double)(BA.ObjectToNumber(_newpunto.Get((Object)("lng"))));
 //BA.debugLineNum = 437;BA.debugLine="Dim tipoEval As String = newpunto.Get(\"tipoeva";
_tipoeval = BA.ObjectToString(_newpunto.Get((Object)("tipoevaluacion")));
 //BA.debugLineNum = 438;BA.debugLine="Dim valorMosquito As String = newpunto.Get(\"va";
_valormosquito = BA.ObjectToString(_newpunto.Get((Object)("valorMosquito")));
 //BA.debugLineNum = 439;BA.debugLine="Dim nombreUsuario As String = newpunto.Get(\"us";
_nombreusuario = BA.ObjectToString(_newpunto.Get((Object)("username")));
 //BA.debugLineNum = 440;BA.debugLine="Dim foto1pathmarker As String = newpunto.Get(\"";
_foto1pathmarker = BA.ObjectToString(_newpunto.Get((Object)("foto1path")));
 //BA.debugLineNum = 441;BA.debugLine="Dim marker As OSMDroid_Marker";
_marker = new uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker();
 //BA.debugLineNum = 442;BA.debugLine="Dim markerSnippet As String";
_markersnippet = "";
 //BA.debugLineNum = 443;BA.debugLine="markerSnippet = nombreUsuario & \"//\" & tipoEva";
_markersnippet = _nombreusuario+"//"+_tipoeval+"////"+_foto1pathmarker;
 //BA.debugLineNum = 444;BA.debugLine="marker.Initialize(valorMosquito,markerSnippet,";
_marker.Initialize(_valormosquito,_markersnippet,_sitiolat,_sitiolong);
 //BA.debugLineNum = 446;BA.debugLine="If tipoEval = \"Criadero\" Then";
if ((_tipoeval).equals("Criadero")) { 
 //BA.debugLineNum = 447;BA.debugLine="marker.SetMarkerIcon(markerCriaderodr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_markercriaderodr.getObject()));
 }else {
 //BA.debugLineNum = 449;BA.debugLine="If valorMosquito = \"aegypti\" Then";
if ((_valormosquito).equals("aegypti")) { 
 //BA.debugLineNum = 450;BA.debugLine="marker.SetMarkerIcon(marker1dr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_marker1dr.getObject()));
 }else if((_valormosquito).equals("albopictus")) { 
 //BA.debugLineNum = 452;BA.debugLine="marker.SetMarkerIcon(marker2dr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_marker2dr.getObject()));
 }else if((_valormosquito).equals("albifasciatus")) { 
 //BA.debugLineNum = 454;BA.debugLine="marker.SetMarkerIcon(marker3dr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_marker3dr.getObject()));
 }else if((_valormosquito).equals("culex")) { 
 //BA.debugLineNum = 456;BA.debugLine="marker.SetMarkerIcon(marker4dr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_marker4dr.getObject()));
 }else {
 //BA.debugLineNum = 458;BA.debugLine="marker.SetMarkerIcon(marker4dr)";
_marker.SetMarkerIcon((android.graphics.drawable.BitmapDrawable)(_marker4dr.getObject()));
 };
 };
 //BA.debugLineNum = 462;BA.debugLine="markersList.Add(marker)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Add((Object)(_marker.getObject()));
 }
};
 //BA.debugLineNum = 466;BA.debugLine="markersOverlay.AddItems(markersList)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.AddItems(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2);
 //BA.debugLineNum = 467;BA.debugLine="MapView1.GetController.SetZoom(7)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (7));
 //BA.debugLineNum = 470;BA.debugLine="Dim ViewHostOverlay1 As OSMDroid_ViewHostOverla";
_viewhostoverlay1 = new uk.co.martinpearman.b4a.osmdroid.views.overlay.ViewHostOverlay();
 //BA.debugLineNum = 471;BA.debugLine="ViewHostOverlay1.Initialize(MapView1)";
_viewhostoverlay1.Initialize(processBA,(org.osmdroid.views.MapView)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()));
 //BA.debugLineNum = 473;BA.debugLine="MarkerInfo.Initialize(\"MarkerInfo\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"MarkerInfo");
 //BA.debugLineNum = 474;BA.debugLine="MarkerInfo.Color=Colors.White";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 475;BA.debugLine="MarkerInfo.TextColor=Colors.DarkGray";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 476;BA.debugLine="MarkerInfo.TextSize=8";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTextSize((float) (8));
 //BA.debugLineNum = 477;BA.debugLine="ViewHost1.Initialize(MarkerInfo, Null, 0, 0, Co";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,(android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),(org.osmdroid.util.GeoPoint)(anywheresoftware.b4a.keywords.Common.Null),(int) (0),(int) (0),mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.LayoutParams.ALIGN_TOP_CENTER,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 478;BA.debugLine="ViewHostOverlay1.AddItem(ViewHost1)";
_viewhostoverlay1.AddItem((uk.co.martinpearman.osmdroid.views.overlay.ViewHost)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()));
 };
 }else {
 //BA.debugLineNum = 484;BA.debugLine="Log(\"GetMapa not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("535061882","GetMapa not ok",0);
 //BA.debugLineNum = 485;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 486;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 488;BA.debugLine="MsgboxAsync(\"There seems to be a problem with t";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with the server, we will fix it soon!"),BA.ObjectToCharSequence("My bad!"),processBA);
 };
 };
 //BA.debugLineNum = 493;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 494;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 496;BA.debugLine="If FusedLocationProvider1.IsConnected = False The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 497;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 };
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 21;BA.debugLine="Private btnDetectar As Button";
mostCurrent._btndetectar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private lblLat As Label";
mostCurrent._lbllat = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private lblLon As Label";
mostCurrent._lbllon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim MapView1 As OSMDroid_MapView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new uk.co.martinpearman.b4a.osmdroid.views.MapView();
 //BA.debugLineNum = 25;BA.debugLine="Private btnZoomAll As Button";
mostCurrent._btnzoomall = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Dim SimpleLocationOverlay1 As OSMDroid_SimpleLoca";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new uk.co.martinpearman.b4a.osmdroid.views.overlay.SimpleLocationOverlay();
 //BA.debugLineNum = 31;BA.debugLine="Private pnlMapa As Panel";
mostCurrent._pnlmapa = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Dim fondoblanco As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim detectandoLabel As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private pnlReferencias As Panel";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim markersList As List";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 38;BA.debugLine="Dim markersOverlay As OSMDroid_MarkerOverlay";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new uk.co.martinpearman.b4a.osmdroid.views.overlay.MarkerOverlay();
 //BA.debugLineNum = 39;BA.debugLine="Private Label4 As Label";
mostCurrent._label4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Dim MarkerInfo As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim Constants1 As OSMDroid_Constants";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new uk.co.martinpearman.b4a.osmdroid.Constants();
 //BA.debugLineNum = 43;BA.debugLine="Dim ViewHost1 As OSMDroid_ViewHost";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new uk.co.martinpearman.b4a.osmdroid.views.overlay.ViewHost();
 //BA.debugLineNum = 46;BA.debugLine="Private pnlConfig As Panel";
mostCurrent._pnlconfig = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private scrConfig As ScrollView";
mostCurrent._scrconfig = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private btnOkConfig As Button";
mostCurrent._btnokconfig = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private btnConfigExplorar As Button";
mostCurrent._btnconfigexplorar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Dim listaConfig As List";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 51;BA.debugLine="Dim listaChequeados As List";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 52;BA.debugLine="Private chkDatosPropios As CheckBox";
mostCurrent._chkdatospropios = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private chkDatosUsuarios As CheckBox";
mostCurrent._chkdatosusuarios = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 54;BA.debugLine="Private btnCheckAll As Button";
mostCurrent._btncheckall = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private lblEspecies As Label";
mostCurrent._lblespecies = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Dim p As Phone";
mostCurrent._vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _locationsettingsresult1_resolutiondialogdismissed(boolean _locationsettingsupdated) throws Exception{
 //BA.debugLineNum = 272;BA.debugLine="Sub LocationSettingsResult1_ResolutionDialogDismis";
 //BA.debugLineNum = 273;BA.debugLine="Log(\"LocationSettingsResult1_ResolutionDialogDism";
anywheresoftware.b4a.keywords.Common.LogImpl("534865153","LocationSettingsResult1_ResolutionDialogDismissed",0);
 //BA.debugLineNum = 274;BA.debugLine="If Not(LocationSettingsUpdated) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_locationsettingsupdated)) { 
 //BA.debugLineNum = 276;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 277;BA.debugLine="MsgboxAsync(\"No tienes habilitada la Localizaci";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No tienes habilitada la Localización, busca en el mapa tu posición"),BA.ObjectToCharSequence("Búsqueda manual"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 279;BA.debugLine="MsgBoxAsync(\"You don't have the location servic";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("You don't have the location services enabled, use the map to locate your position"),BA.ObjectToCharSequence("Manual search"),processBA);
 };
 //BA.debugLineNum = 284;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 285;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 286;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 289;BA.debugLine="If FusedLocationProvider1.IsConnected = True The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 290;BA.debugLine="FusedLocationProvider1.DisConnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 //BA.debugLineNum = 293;BA.debugLine="If fondoblanco.IsInitialized Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.IsInitialized()) { 
 //BA.debugLineNum = 294;BA.debugLine="fondoblanco.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.RemoveView();
 //BA.debugLineNum = 295;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 };
 };
 //BA.debugLineNum = 298;BA.debugLine="End Sub";
return "";
}
public static boolean  _markersoverlay_click(uk.co.martinpearman.b4a.osmdroid.views.overlay.Marker _marker1) throws Exception{
String _snippet = "";
String _tipoeval = "";
String _fotopath = "";
 //BA.debugLineNum = 507;BA.debugLine="Sub markersOverlay_Click(Marker1 As OSMDroid_Marke";
 //BA.debugLineNum = 508;BA.debugLine="MarkerInfo.Color = Colors.White";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 509;BA.debugLine="ViewHost1.SetPosition(Marker1.GetPoint)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.SetPosition((org.osmdroid.util.GeoPoint)(_marker1.GetPoint().getObject()));
 //BA.debugLineNum = 511;BA.debugLine="Dim snippet As String = Marker1.GetSnippet.SubStr";
_snippet = _marker1.GetSnippet().substring((int) (0),_marker1.GetSnippet().indexOf("//"));
 //BA.debugLineNum = 512;BA.debugLine="Dim tipoeval As String = Marker1.GetSnippet.SubSt";
_tipoeval = _marker1.GetSnippet().substring((int) (_marker1.GetSnippet().indexOf("//")+2),_marker1.GetSnippet().indexOf("////"));
 //BA.debugLineNum = 513;BA.debugLine="Dim fotopath As String = Marker1.GetSnippet.SubSt";
_fotopath = _marker1.GetSnippet().substring((int) (_marker1.GetSnippet().indexOf("////")+4),_marker1.GetSnippet().length());
 //BA.debugLineNum = 516;BA.debugLine="If tipoeval = \"Criadero\" Then";
if ((_tipoeval).equals("Criadero")) { 
 //BA.debugLineNum = 517;BA.debugLine="MarkerInfo.Text=\"Criadero \" & CRLF & \"Contribuci";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Criadero "+anywheresoftware.b4a.keywords.Common.CRLF+"Contribución: "+_snippet));
 }else {
 //BA.debugLineNum = 521;BA.debugLine="fotopath = fotopath.SubString2(2,fotopath.Length";
_fotopath = _fotopath.substring((int) (2),_fotopath.length());
 //BA.debugLineNum = 522;BA.debugLine="MarkerInfo.Text=\"Aedes \" & Marker1.GetTitle& CRL";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Aedes "+_marker1.GetTitle()+anywheresoftware.b4a.keywords.Common.CRLF+"Contribución: "+_snippet));
 };
 //BA.debugLineNum = 528;BA.debugLine="If Not(ViewHost1.Visible) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getVisible())) { 
 //BA.debugLineNum = 529;BA.debugLine="ViewHost1.Visible=True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 531;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 532;BA.debugLine="End Sub";
return false;
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private FusedLocationProvider1 As FusedLocationPr";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new uk.co.martinpearman.b4a.fusedlocationprovider.FusedLocationProviderWrapper();
 //BA.debugLineNum = 10;BA.debugLine="Private LastLocation As Location";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim rp As RuntimePermissions";
_v6 = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 17;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5() throws Exception{
uk.co.martinpearman.b4a.osmdroid.util.GeoPoint _geopoint1 = null;
 //BA.debugLineNum = 299;BA.debugLine="Sub UpdateUI";
 //BA.debugLineNum = 301;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 302;BA.debugLine="ToastMessageShow(\"Posición encontrada!\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Posición encontrada!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 304;BA.debugLine="ToastMessageShow(\"Location found!\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Location found!"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 307;BA.debugLine="lblLat.Text = LastLocation.Latitude";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()));
 //BA.debugLineNum = 308;BA.debugLine="lblLon.Text = LastLocation.Longitude";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()));
 //BA.debugLineNum = 309;BA.debugLine="Dim geopoint1 As OSMDroid_GeoPoint";
_geopoint1 = new uk.co.martinpearman.b4a.osmdroid.util.GeoPoint();
 //BA.debugLineNum = 310;BA.debugLine="geopoint1.Initialize(lblLat.Text,lblLon.Text)";
_geopoint1.Initialize((double)(Double.parseDouble(mostCurrent._lbllat.getText())),(double)(Double.parseDouble(mostCurrent._lbllon.getText())));
 //BA.debugLineNum = 313;BA.debugLine="If SimpleLocationOverlay1.IsInitialized = False T";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 314;BA.debugLine="SimpleLocationOverlay1.Initialize";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(processBA);
 };
 //BA.debugLineNum = 316;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 319;BA.debugLine="MapView1.GetController.SetCenter(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetCenter((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 320;BA.debugLine="MapView1.GetController.SetZoom(14)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (14));
 //BA.debugLineNum = 321;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 322;BA.debugLine="btnDetectar.TextColor = Colors.Black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 324;BA.debugLine="FusedLocationProvider1.Disconnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 //BA.debugLineNum = 325;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 327;BA.debugLine="If fondoblanco.IsInitialized Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.IsInitialized()) { 
 //BA.debugLineNum = 328;BA.debugLine="fondoblanco.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.RemoveView();
 //BA.debugLineNum = 329;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 }else {
 //BA.debugLineNum = 331;BA.debugLine="btnDetectar.Color = Colors.Black";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 332;BA.debugLine="btnDetectar.TextColor = Colors.White";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 };
 //BA.debugLineNum = 334;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 335;BA.debugLine="End Sub";
return "";
}
}
