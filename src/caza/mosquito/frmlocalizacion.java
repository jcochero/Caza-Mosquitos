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

public class frmlocalizacion extends Activity implements B4AActivity{
	public static frmlocalizacion mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmlocalizacion");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmlocalizacion).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmlocalizacion");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmlocalizacion", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmlocalizacion) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmlocalizacion) Resume **");
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
		return frmlocalizacion.class;
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
            BA.LogInfo("** Activity (frmlocalizacion) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmlocalizacion) Pause event (activity is not paused). **");
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
            frmlocalizacion mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmlocalizacion) Resume **");
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
public static String _vvvvvvvv4 = "";
public static String _vvvvvvvv5 = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btndetectar = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllon = null;
public uk.co.martinpearman.b4a.osmdroid.views.MapView _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public uk.co.martinpearman.b4a.osmdroid.views.overlay.SimpleLocationOverlay _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmapa = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmanual = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncontinuarlocalizacion = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = null;
public anywheresoftware.b4a.objects.RuntimePermissions _v6 = null;
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
public caza.mosquito.frmmapa _vvvvvvvvvvvvvvvvvvvv0 = null;
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
 //BA.debugLineNum = 35;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 36;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 37;BA.debugLine="ProgressDialogShow(\"Buscando posición...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Buscando posición..."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 39;BA.debugLine="ProgressDialogShow(\"Searching position...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Searching position..."));
 };
 //BA.debugLineNum = 43;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._vvvvvvv6.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 44;BA.debugLine="If FusedLocationProvider1.IsInitialized = False T";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 45;BA.debugLine="FusedLocationProvider1.Initialize(\"FusedLocation";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(processBA,"FusedLocationProvider1");
 };
 //BA.debugLineNum = 48;BA.debugLine="If tipoDetect <> \"MAPAdetect\" Then";
if ((_vvvvvvvv4).equals("MAPAdetect") == false) { 
 //BA.debugLineNum = 49;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 };
 //BA.debugLineNum = 52;BA.debugLine="Activity.LoadLayout(\"frmlocation\")";
mostCurrent._activity.LoadLayout("frmlocation",mostCurrent.activityBA);
 //BA.debugLineNum = 53;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 54;BA.debugLine="Load_Reporte_Localizacion";
_load_reporte_localizacion();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 60;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 61;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 62;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 };
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 69;BA.debugLine="If FusedLocationProvider1.IsConnected = True Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 70;BA.debugLine="FusedLocationProvider1.Disconnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 66;BA.debugLine="TranslateGUI";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _btncontinuarlocalizacion_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 385;BA.debugLine="Sub btnContinuarLocalizacion_Click";
 //BA.debugLineNum = 387;BA.debugLine="If lblLat.Text = \"\" Or lblLat.Text = \"0\" Then";
if ((mostCurrent._lbllat.getText()).equals("") || (mostCurrent._lbllat.getText()).equals("0")) { 
 //BA.debugLineNum = 388;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 389;BA.debugLine="ToastMessageShow(\"Debe buscar su posición para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Debe buscar su posición para seguir"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 391;BA.debugLine="ToastMessageShow(\"You must mark your location t";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You must mark your location to continue"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 393;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 397;BA.debugLine="If origen = \"cambio\" Then";
if ((_vvvvvvvv5).equals("cambio")) { 
 //BA.debugLineNum = 398;BA.debugLine="frmDatosAnteriores.nuevalatlng = lblLat.Text & \"";
mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv2 /*String*/  = mostCurrent._lbllat.getText()+"_"+mostCurrent._lbllon.getText();
 //BA.debugLineNum = 399;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 400;BA.debugLine="frmDatosAnteriores.notificacion = False";
mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv3 /*boolean*/  = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 401;BA.debugLine="CallSubDelayed(frmDatosAnteriores, \"CambiarUbica";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv6.getObject()),"CambiarUbicacion");
 //BA.debugLineNum = 403;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 404;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 409;BA.debugLine="If lblLat.Text = \"\" Or lblLon.Text = \"\" Then";
if ((mostCurrent._lbllat.getText()).equals("") || (mostCurrent._lbllon.getText()).equals("")) { 
 //BA.debugLineNum = 410;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 411;BA.debugLine="ToastMessageShow(\"No se han detectado tus coord";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No se han detectado tus coordenadas, intenta de nuevo!"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 413;BA.debugLine="ToastMessageShow(\"Your coordinates have not bee";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Your coordinates have not been found, try again!"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 415;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 417;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 418;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 419;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 421;BA.debugLine="Main.latitud = lblLat.Text";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv5 /*String*/  = mostCurrent._lbllat.getText();
 //BA.debugLineNum = 422;BA.debugLine="Main.longitud = lblLon.Text";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv4 /*String*/  = mostCurrent._lbllon.getText();
 //BA.debugLineNum = 423;BA.debugLine="Main.dateandtime = DateTime.Date(DateTime.Now)";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv2 /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 425;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLatitude",(Object)(mostCurrent._lbllat.getText()),_map1);
 //BA.debugLineNum = 426;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","decimalLongitude",(Object)(mostCurrent._lbllon.getText()),_map1);
 //BA.debugLineNum = 427;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loca";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","georeferencedDate",(Object)(anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())),_map1);
 //BA.debugLineNum = 429;BA.debugLine="If tipoDetect = \"GPSdetect\" Then";
if ((_vvvvvvvv4).equals("GPSdetect")) { 
 //BA.debugLineNum = 430;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","gpsDetect",(Object)("si"),_map1);
 }else if((_vvvvvvvv4).equals("MAPAdetect")) { 
 //BA.debugLineNum = 432;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","mapaDetect",(Object)("si"),_map1);
 };
 //BA.debugLineNum = 436;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 437;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 439;BA.debugLine="If Main.tipoevaluacion = \"Mosquito\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv2 /*String*/ ).equals("Mosquito")) { 
 //BA.debugLineNum = 440;BA.debugLine="StartActivity(frmFotos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv3.getObject()));
 }else {
 //BA.debugLineNum = 442;BA.debugLine="StartActivity(frmFotosCriadero)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv4.getObject()));
 };
 //BA.debugLineNum = 444;BA.debugLine="End Sub";
return "";
}
public static String  _btndetectar_click() throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Sub btnDetectar_Click";
 //BA.debugLineNum = 144;BA.debugLine="btnDetectar.Color = Colors.Black";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 145;BA.debugLine="btnDetectar.TextColor = Colors.White";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 146;BA.debugLine="btnManual.TextColor = Colors.Black";
mostCurrent._btnmanual.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 147;BA.debugLine="btnManual.Color = Colors.Transparent";
mostCurrent._btnmanual.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 148;BA.debugLine="DetectarPosicion";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7();
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _btnmanual_click() throws Exception{
 //BA.debugLineNum = 341;BA.debugLine="Sub btnManual_Click";
 //BA.debugLineNum = 342;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 343;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 344;BA.debugLine="btnManual.TextColor = Colors.white";
mostCurrent._btnmanual.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 345;BA.debugLine="btnManual.Color = Colors.Black";
mostCurrent._btnmanual.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 349;BA.debugLine="If FusedLocationProvider1.IsConnected = True Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 350;BA.debugLine="FusedLocationProvider1.DisConnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 //BA.debugLineNum = 354;BA.debugLine="tipoDetect = \"MAPAdetect\"";
_vvvvvvvv4 = "MAPAdetect";
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0() throws Exception{
uk.co.martinpearman.b4a.osmdroid.util.GeoPoint _geopoint1 = null;
 //BA.debugLineNum = 110;BA.debugLine="Sub CargarMapa";
 //BA.debugLineNum = 111;BA.debugLine="If LastLocation.IsInitialized Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.IsInitialized()) { 
 //BA.debugLineNum = 112;BA.debugLine="lblLat.Text = LastLocation.Latitude";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()));
 //BA.debugLineNum = 113;BA.debugLine="lblLon.Text = LastLocation.Longitude";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()));
 }else {
 //BA.debugLineNum = 115;BA.debugLine="lblLat.Text = \"-34.9204950\"";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence("-34.9204950"));
 //BA.debugLineNum = 116;BA.debugLine="lblLon.Text = \"-57.9535660\"";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence("-57.9535660"));
 };
 //BA.debugLineNum = 118;BA.debugLine="MapView1.Initialize(\"MapView1\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"MapView1");
 //BA.debugLineNum = 119;BA.debugLine="MapView1.SetBuiltInZoomControls(False)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.SetBuiltInZoomControls(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 120;BA.debugLine="MapView1.SetMultiTouchControls(True)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.SetMultiTouchControls(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 122;BA.debugLine="Dim geopoint1 As OSMDroid_GeoPoint";
_geopoint1 = new uk.co.martinpearman.b4a.osmdroid.util.GeoPoint();
 //BA.debugLineNum = 123;BA.debugLine="geopoint1.Initialize(lblLat.Text, lblLon.Text)";
_geopoint1.Initialize((double)(Double.parseDouble(mostCurrent._lbllat.getText())),(double)(Double.parseDouble(mostCurrent._lbllon.getText())));
 //BA.debugLineNum = 124;BA.debugLine="MapView1.GetController.SetCenter(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetCenter((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 125;BA.debugLine="MapView1.GetController.SetZoom(10)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (10));
 //BA.debugLineNum = 126;BA.debugLine="pnlMapa.AddView(MapView1, 0,0, 100%x, 100%y)";
mostCurrent._pnlmapa.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 127;BA.debugLine="If SimpleLocationOverlay1.IsInitialized = False T";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 128;BA.debugLine="SimpleLocationOverlay1.Initialize()";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(processBA);
 //BA.debugLineNum = 129;BA.debugLine="MapView1.GetOverlays.Add(SimpleLocationOverlay1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()));
 //BA.debugLineNum = 130;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 }else {
 //BA.debugLineNum = 132;BA.debugLine="MapView1.GetOverlays.Add(SimpleLocationOverlay1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetOverlays().Add((org.osmdroid.views.overlay.Overlay)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.getObject()));
 //BA.debugLineNum = 133;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 };
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7() throws Exception{
 //BA.debugLineNum = 153;BA.debugLine="Sub DetectarPosicion";
 //BA.debugLineNum = 156;BA.debugLine="fondogris.Initialize(\"fondogris\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.Initialize(mostCurrent.activityBA,"fondogris");
 //BA.debugLineNum = 157;BA.debugLine="detectandoLabel.Initialize(\"\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 158;BA.debugLine="detectandoLabel.TextColor = Colors.White";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 159;BA.debugLine="detectandoLabel.Gravity = Gravity.CENTER_HORIZONT";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 160;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 161;BA.debugLine="detectandoLabel.Text = \"Buscando tu posición aut";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Buscando tu posición automáticamente..."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 163;BA.debugLine="detectandoLabel.Text = \"Searching your position";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.setText(BA.ObjectToCharSequence("Searching your position automatically..."));
 };
 //BA.debugLineNum = 168;BA.debugLine="btnContinuarLocalizacion.Visible = False";
mostCurrent._btncontinuarlocalizacion.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 170;BA.debugLine="fondogris.Color = Colors.ARGB(150, 64,64,64)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (64),(int) (64),(int) (64)));
 //BA.debugLineNum = 171;BA.debugLine="Activity.AddView(fondogris, 0,0, 100%x, 100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 172;BA.debugLine="Activity.AddView(detectandoLabel, 5%x, 45%y, 80%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (5),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (45),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 173;BA.debugLine="btnDetectar.Enabled = False";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 175;BA.debugLine="If FusedLocationProvider1.IsInitialized = False T";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 176;BA.debugLine="FusedLocationProvider1.Initialize(\"FusedLocation";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Initialize(processBA,"FusedLocationProvider1");
 };
 //BA.debugLineNum = 179;BA.debugLine="If FusedLocationProvider1.IsConnected = False The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 180;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 };
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _fondogris_click() throws Exception{
 //BA.debugLineNum = 186;BA.debugLine="Sub fondogris_Click";
 //BA.debugLineNum = 188;BA.debugLine="tipoDetect = \"MAPAdetect\"";
_vvvvvvvv4 = "MAPAdetect";
 //BA.debugLineNum = 189;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 190;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 191;BA.debugLine="btnManual.TextColor = Colors.white";
mostCurrent._btnmanual.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 192;BA.debugLine="btnManual.Color = Colors.Black";
mostCurrent._btnmanual.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 193;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 195;BA.debugLine="fondogris.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.RemoveView();
 //BA.debugLineNum = 196;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 //BA.debugLineNum = 197;BA.debugLine="btnContinuarLocalizacion.Visible = True";
mostCurrent._btncontinuarlocalizacion.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionfailed(int _connectionresult1) throws Exception{
 //BA.debugLineNum = 201;BA.debugLine="Sub FusedLocationProvider1_ConnectionFailed(Connec";
 //BA.debugLineNum = 202;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionFailed\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533161217","FusedLocationProvider1_ConnectionFailed",0);
 //BA.debugLineNum = 204;BA.debugLine="Select ConnectionResult1";
switch (BA.switchObjectToInt(_connectionresult1,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.ConnectionResult.NETWORK_ERROR)) {
case 0: {
 //BA.debugLineNum = 206;BA.debugLine="FusedLocationProvider1.Connect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Connect();
 break; }
default: {
 break; }
}
;
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionsuccess() throws Exception{
uk.co.martinpearman.b4a.fusedlocationprovider.LocationRequest _locationrequest1 = null;
uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsRequestBuilder _locationsettingsrequestbuilder1 = null;
 //BA.debugLineNum = 211;BA.debugLine="Sub FusedLocationProvider1_ConnectionSuccess";
 //BA.debugLineNum = 212;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionSuccess\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533226753","FusedLocationProvider1_ConnectionSuccess",0);
 //BA.debugLineNum = 213;BA.debugLine="Dim LocationRequest1 As LocationRequest";
_locationrequest1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationRequest();
 //BA.debugLineNum = 214;BA.debugLine="LocationRequest1.Initialize";
_locationrequest1.Initialize();
 //BA.debugLineNum = 215;BA.debugLine="LocationRequest1.SetInterval(1000)	'	1000 millise";
_locationrequest1.SetInterval((long) (1000));
 //BA.debugLineNum = 216;BA.debugLine="LocationRequest1.SetPriority(LocationRequest1.Pri";
_locationrequest1.SetPriority(_locationrequest1.Priority.PRIORITY_HIGH_ACCURACY);
 //BA.debugLineNum = 217;BA.debugLine="LocationRequest1.SetSmallestDisplacement(1)	'	1 m";
_locationrequest1.SetSmallestDisplacement((float) (1));
 //BA.debugLineNum = 219;BA.debugLine="Dim LocationSettingsRequestBuilder1 As LocationSe";
_locationsettingsrequestbuilder1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsRequestBuilder();
 //BA.debugLineNum = 220;BA.debugLine="LocationSettingsRequestBuilder1.Initialize";
_locationsettingsrequestbuilder1.Initialize();
 //BA.debugLineNum = 221;BA.debugLine="LocationSettingsRequestBuilder1.AddLocationReques";
_locationsettingsrequestbuilder1.AddLocationRequest((com.google.android.gms.location.LocationRequest)(_locationrequest1.getObject()));
 //BA.debugLineNum = 222;BA.debugLine="FusedLocationProvider1.CheckLocationSettings(Loca";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.CheckLocationSettings(_locationsettingsrequestbuilder1.Build());
 //BA.debugLineNum = 224;BA.debugLine="FusedLocationProvider1.RequestLocationUpdates(Loc";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.RequestLocationUpdates((com.google.android.gms.location.LocationRequest)(_locationrequest1.getObject()));
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_connectionsuspended(int _suspendedcause1) throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub FusedLocationProvider1_ConnectionSuspended(Sus";
 //BA.debugLineNum = 227;BA.debugLine="Log(\"FusedLocationProvider1_ConnectionSuspended\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533292289","FusedLocationProvider1_ConnectionSuspended",0);
 //BA.debugLineNum = 228;BA.debugLine="Select SuspendedCause1";
switch (BA.switchObjectToInt(_suspendedcause1,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SuspendedCause.CAUSE_NETWORK_LOST,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.SuspendedCause.CAUSE_SERVICE_DISCONNECTED)) {
case 0: {
 break; }
case 1: {
 break; }
}
;
 //BA.debugLineNum = 234;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub FusedLocationProvider1_LocationChanged(Locatio";
 //BA.debugLineNum = 236;BA.debugLine="Log(\"FusedLocationProvider1_LocationChanged\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533357825","FusedLocationProvider1_LocationChanged",0);
 //BA.debugLineNum = 237;BA.debugLine="LastLocation=Location1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = _location1;
 //BA.debugLineNum = 238;BA.debugLine="Log(LastLocation.Latitude)";
anywheresoftware.b4a.keywords.Common.LogImpl("533357827",BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()),0);
 //BA.debugLineNum = 239;BA.debugLine="Log(LastLocation.Longitude)";
anywheresoftware.b4a.keywords.Common.LogImpl("533357828",BA.NumberToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()),0);
 //BA.debugLineNum = 240;BA.debugLine="UpdateUI";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5();
 //BA.debugLineNum = 241;BA.debugLine="End Sub";
return "";
}
public static String  _fusedlocationprovider1_locationsettingschecked(uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsResult _locationsettingsresult1) throws Exception{
uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsStatus _locationsettingsstatus1 = null;
 //BA.debugLineNum = 242;BA.debugLine="Sub FusedLocationProvider1_LocationSettingsChecked";
 //BA.debugLineNum = 243;BA.debugLine="Log(\"FusedLocationProvider1_LocationSettingsCheck";
anywheresoftware.b4a.keywords.Common.LogImpl("533423361","FusedLocationProvider1_LocationSettingsChecked",0);
 //BA.debugLineNum = 244;BA.debugLine="Dim LocationSettingsStatus1 As LocationSettingsSt";
_locationsettingsstatus1 = new uk.co.martinpearman.b4a.fusedlocationprovider.LocationSettingsStatus();
_locationsettingsstatus1 = _locationsettingsresult1.GetLocationSettingsStatus();
 //BA.debugLineNum = 245;BA.debugLine="Select LocationSettingsStatus1.GetStatusCode";
switch (BA.switchObjectToInt(_locationsettingsstatus1.GetStatusCode(),_locationsettingsstatus1.StatusCodes.RESOLUTION_REQUIRED,_locationsettingsstatus1.StatusCodes.SETTINGS_CHANGE_UNAVAILABLE,_locationsettingsstatus1.StatusCodes.SUCCESS)) {
case 0: {
 //BA.debugLineNum = 247;BA.debugLine="Log(\"RESOLUTION_REQUIRED\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533423365","RESOLUTION_REQUIRED",0);
 //BA.debugLineNum = 250;BA.debugLine="LocationSettingsStatus1.StartResolutionDialog(\"";
_locationsettingsstatus1.StartResolutionDialog(mostCurrent.activityBA,"LocationSettingsResult1");
 break; }
case 1: {
 //BA.debugLineNum = 252;BA.debugLine="Log(\"SETTINGS_CHANGE_UNAVAILABLE\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533423370","SETTINGS_CHANGE_UNAVAILABLE",0);
 //BA.debugLineNum = 255;BA.debugLine="MsgBoxAsync(\"Error, tu dispositivo no tiene loc";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Error, tu dispositivo no tiene localización. Busca tu posición en el mapa!"),BA.ObjectToCharSequence("Problem"),processBA);
 //BA.debugLineNum = 256;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 break; }
case 2: {
 //BA.debugLineNum = 258;BA.debugLine="Log(\"SUCCESS\")";
anywheresoftware.b4a.keywords.Common.LogImpl("533423376","SUCCESS",0);
 break; }
}
;
 //BA.debugLineNum = 262;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private btnDetectar As Button";
mostCurrent._btndetectar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private lblLat As Label";
mostCurrent._lbllat = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private lblLon As Label";
mostCurrent._lbllon = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim MapView1 As OSMDroid_MapView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new uk.co.martinpearman.b4a.osmdroid.views.MapView();
 //BA.debugLineNum = 21;BA.debugLine="Dim SimpleLocationOverlay1 As OSMDroid_SimpleLoca";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new uk.co.martinpearman.b4a.osmdroid.views.overlay.SimpleLocationOverlay();
 //BA.debugLineNum = 23;BA.debugLine="Private pnlMapa As Panel";
mostCurrent._pnlmapa = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private btnManual As Button";
mostCurrent._btnmanual = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private btnContinuarLocalizacion As Button";
mostCurrent._btncontinuarlocalizacion = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Dim fondogris As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Dim detectandoLabel As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Dim rp As RuntimePermissions";
mostCurrent._v6 = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 32;BA.debugLine="Dim p As Phone";
mostCurrent._vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 34;BA.debugLine="End Sub";
return "";
}
public static String  _load_reporte_localizacion() throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Sub Load_Reporte_Localizacion";
 //BA.debugLineNum = 91;BA.debugLine="If origen <> \"cambio\" Then";
if ((_vvvvvvvv5).equals("cambio") == false) { 
 //BA.debugLineNum = 92;BA.debugLine="origen = \"Reporte_Localizacion\"";
_vvvvvvvv5 = "Reporte_Localizacion";
 //BA.debugLineNum = 93;BA.debugLine="lblLat.Text = \"\"";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 94;BA.debugLine="lblLon.Text = \"\"";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(""));
 };
 //BA.debugLineNum = 97;BA.debugLine="CargarMapa";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _locationsettingsresult1_resolutiondialogdismissed(boolean _locationsettingsupdated) throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Sub LocationSettingsResult1_ResolutionDialogDismis";
 //BA.debugLineNum = 264;BA.debugLine="Log(\"LocationSettingsResult1_ResolutionDialogDism";
anywheresoftware.b4a.keywords.Common.LogImpl("533488897","LocationSettingsResult1_ResolutionDialogDismissed",0);
 //BA.debugLineNum = 265;BA.debugLine="If Not(LocationSettingsUpdated) Then";
if (anywheresoftware.b4a.keywords.Common.Not(_locationsettingsupdated)) { 
 //BA.debugLineNum = 267;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 268;BA.debugLine="MsgboxAsync(\"No tienes habilitada la Localizaci";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No tienes habilitada la Localización, busca en el mapa tu posición"),BA.ObjectToCharSequence("Búsqueda manual"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 270;BA.debugLine="MsgboxAsync(\"Your location services are disable";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Your location services are disabled, find it manually by moving the map"),BA.ObjectToCharSequence("Manual search"),processBA);
 };
 //BA.debugLineNum = 274;BA.debugLine="btnDetectar.Color = Colors.Transparent";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 275;BA.debugLine="btnDetectar.TextColor = Colors.black";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 276;BA.debugLine="btnManual.TextColor = Colors.white";
mostCurrent._btnmanual.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 277;BA.debugLine="btnManual.Color = Colors.Black";
mostCurrent._btnmanual.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 278;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 281;BA.debugLine="If FusedLocationProvider1.IsConnected = True The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 282;BA.debugLine="FusedLocationProvider1.DisConnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 //BA.debugLineNum = 285;BA.debugLine="tipoDetect = \"MAPAdetect\"";
_vvvvvvvv4 = "MAPAdetect";
 //BA.debugLineNum = 287;BA.debugLine="If fondogris.IsInitialized Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.IsInitialized()) { 
 //BA.debugLineNum = 288;BA.debugLine="fondogris.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.RemoveView();
 //BA.debugLineNum = 289;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 };
 };
 //BA.debugLineNum = 292;BA.debugLine="End Sub";
return "";
}
public static String  _mapview1_centerchanged() throws Exception{
 //BA.debugLineNum = 358;BA.debugLine="Sub MapView1_CenterChanged";
 //BA.debugLineNum = 360;BA.debugLine="If tipoDetect = \"GPSdetect\" Then";
if ((_vvvvvvvv4).equals("GPSdetect")) { 
 //BA.debugLineNum = 364;BA.debugLine="If FusedLocationProvider1.IsConnected = True The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 365;BA.debugLine="FusedLocationProvider1.Disconnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 }else if((_vvvvvvvv4).equals("MAPAdetect")) { 
 //BA.debugLineNum = 368;BA.debugLine="If FusedLocationProvider1.IsConnected = True The";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.IsConnected()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 369;BA.debugLine="FusedLocationProvider1.Disconnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 };
 //BA.debugLineNum = 372;BA.debugLine="If SimpleLocationOverlay1.IsInitialized = False";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 373;BA.debugLine="SimpleLocationOverlay1.Initialize()";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(processBA);
 };
 //BA.debugLineNum = 375;BA.debugLine="SimpleLocationOverlay1.SetLocation(MapView1.GetM";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetMapCenter().getObject()));
 //BA.debugLineNum = 376;BA.debugLine="lblLat.Text = MapView1.GetMapCenter.Latitude";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetMapCenter().getLatitude()));
 //BA.debugLineNum = 377;BA.debugLine="lblLon.Text = MapView1.GetMapCenter.Longitude";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetMapCenter().getLongitude()));
 };
 //BA.debugLineNum = 380;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Private FusedLocationProvider1 As FusedLocationPr";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = new uk.co.martinpearman.b4a.fusedlocationprovider.FusedLocationProviderWrapper();
 //BA.debugLineNum = 9;BA.debugLine="Private LastLocation As Location";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim tipoDetect As String";
_vvvvvvvv4 = "";
 //BA.debugLineNum = 13;BA.debugLine="Dim origen As String";
_vvvvvvvv5 = "";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
 //BA.debugLineNum = 81;BA.debugLine="Sub TranslateGUI";
 //BA.debugLineNum = 82;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 83;BA.debugLine="btnContinuarLocalizacion.Text = \"Continuar \"";
mostCurrent._btncontinuarlocalizacion.setText(BA.ObjectToCharSequence("Continuar "));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 85;BA.debugLine="btnContinuarLocalizacion.Text = \"Continue \"";
mostCurrent._btncontinuarlocalizacion.setText(BA.ObjectToCharSequence("Continue "));
 };
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5() throws Exception{
uk.co.martinpearman.b4a.osmdroid.util.GeoPoint _geopoint1 = null;
 //BA.debugLineNum = 294;BA.debugLine="Sub UpdateUI";
 //BA.debugLineNum = 295;BA.debugLine="tipoDetect = \"GPSdetect\"";
_vvvvvvvv4 = "GPSdetect";
 //BA.debugLineNum = 296;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 297;BA.debugLine="ToastMessageShow(\"Posición encontrada!\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Posición encontrada!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 299;BA.debugLine="ToastMessageShow(\"Position found!\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Position found!"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 302;BA.debugLine="lblLat.Text = LastLocation.Latitude";
mostCurrent._lbllat.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLatitude()));
 //BA.debugLineNum = 303;BA.debugLine="lblLon.Text = LastLocation.Longitude";
mostCurrent._lbllon.setText(BA.ObjectToCharSequence(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getLongitude()));
 //BA.debugLineNum = 306;BA.debugLine="If SimpleLocationOverlay1.IsInitialized = False T";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 307;BA.debugLine="SimpleLocationOverlay1.Initialize()";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.Initialize(processBA);
 };
 //BA.debugLineNum = 309;BA.debugLine="Dim geopoint1 As OSMDroid_GeoPoint";
_geopoint1 = new uk.co.martinpearman.b4a.osmdroid.util.GeoPoint();
 //BA.debugLineNum = 310;BA.debugLine="geopoint1.Initialize(lblLat.Text, lblLon.Text)";
_geopoint1.Initialize((double)(Double.parseDouble(mostCurrent._lbllat.getText())),(double)(Double.parseDouble(mostCurrent._lbllon.getText())));
 //BA.debugLineNum = 311;BA.debugLine="SimpleLocationOverlay1.SetLocation(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.SetLocation((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 314;BA.debugLine="MapView1.GetController.SetCenter(geopoint1)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetCenter((org.osmdroid.util.GeoPoint)(_geopoint1.getObject()));
 //BA.debugLineNum = 315;BA.debugLine="MapView1.GetController.SetZoom(14)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetController().SetZoom((int) (14));
 //BA.debugLineNum = 318;BA.debugLine="FusedLocationProvider1.Disconnect";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6.Disconnect();
 //BA.debugLineNum = 319;BA.debugLine="btnDetectar.Enabled = True";
mostCurrent._btndetectar.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 321;BA.debugLine="If fondogris.IsInitialized Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.IsInitialized()) { 
 //BA.debugLineNum = 322;BA.debugLine="fondogris.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5.RemoveView();
 //BA.debugLineNum = 323;BA.debugLine="detectandoLabel.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4.RemoveView();
 }else {
 //BA.debugLineNum = 325;BA.debugLine="btnDetectar.Color = Colors.Black";
mostCurrent._btndetectar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 326;BA.debugLine="btnDetectar.TextColor = Colors.White";
mostCurrent._btndetectar.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 327;BA.debugLine="btnManual.TextColor = Colors.Black";
mostCurrent._btnmanual.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 328;BA.debugLine="btnManual.Color = Colors.Transparent";
mostCurrent._btnmanual.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 };
 //BA.debugLineNum = 330;BA.debugLine="btnContinuarLocalizacion.Visible = True";
mostCurrent._btncontinuarlocalizacion.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 331;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
}
