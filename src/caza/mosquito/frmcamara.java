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

public class frmcamara extends Activity implements B4AActivity{
	public static frmcamara mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmcamara");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmcamara).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmcamara");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmcamara", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmcamara) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmcamara) Resume **");
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
		return frmcamara.class;
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
            BA.LogInfo("** Activity (frmcamara) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmcamara) Pause event (activity is not paused). **");
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
            frmcamara mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmcamara) Resume **");
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
public static boolean _vvvvv0 = false;
public static anywheresoftware.b4h.okhttp.OkHttpClientWrapper _vvvvvv1 = null;
public static anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _vvvvvv2 = null;
public static boolean _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = false;
public static anywheresoftware.b4a.phone.Phone.ContentChooser _vvvvvv3 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv4 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv5 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv6 = null;
public static com.spinter.uploadfilephp.UploadFilePhp _vvvvvv7 = null;
public static String _vvvvvv0 = "";
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public caza.mosquito.cameraexclass _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btntakepicture = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgflash = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnadjuntarfoto = null;
public anywheresoftware.b4a.objects.LabelWrapper _imgmenu = null;
public anywheresoftware.b4a.objects.SeekBarWrapper _seekfocus = null;
public static String _vvvvvvv0 = "";
public static String _vvvvvvvv1 = "";
public static String _vvvvvvvv2 = "";
public static String _vvvvvvvv3 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
public anywheresoftware.b4a.objects.LabelWrapper _lbltemplate = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgtemplate = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
public static int _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = 0;
public anywheresoftware.b4a.objects.ImageViewWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnnofoto = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnyesfoto = null;
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

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 57;BA.debugLine="Activity.LoadLayout(\"layCamera\")";
mostCurrent._activity.LoadLayout("layCamera",mostCurrent.activityBA);
 //BA.debugLineNum = 58;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 60;BA.debugLine="If File.ExternalWritable Then";
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()) { 
 //BA.debugLineNum = 61;BA.debugLine="If File.IsDirectory(File.DirRootExternal,\"CazaMo";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"CazaMosquitos/")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 62;BA.debugLine="File.MakeDir(File.DirRootExternal, \"CazaMosquit";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"CazaMosquitos");
 };
 }else {
 //BA.debugLineNum = 65;BA.debugLine="If File.IsDirectory(File.DirInternal,\"CazaMosqui";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"CazaMosquitos/")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 66;BA.debugLine="File.MakeDir(File.DirInternal, \"CazaMosquitos\")";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"CazaMosquitos");
 };
 };
 //BA.debugLineNum = 71;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 72;BA.debugLine="Activity.AddMenuItem(\"Consejos para sacar fotos\"";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Consejos para sacar fotos"),"mnuConsejos");
 //BA.debugLineNum = 73;BA.debugLine="Activity.AddMenuItem(\"Adjuntar foto\", \"mnuAdjunt";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Adjuntar foto"),"mnuAdjuntar");
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 75;BA.debugLine="Activity.AddMenuItem(\"How to take better photos\"";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("How to take better photos"),"mnuConsejos");
 //BA.debugLineNum = 76;BA.debugLine="Activity.AddMenuItem(\"Attach photo\", \"mnuAdjunta";
mostCurrent._activity.AddMenuItem(BA.ObjectToCharSequence("Attach photo"),"mnuAdjuntar");
 };
 //BA.debugLineNum = 81;BA.debugLine="If hc.IsInitialized = False Then";
if (_vvvvvv1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 82;BA.debugLine="hc.Initialize(\"hc\")";
_vvvvvv1.Initialize("hc");
 };
 //BA.debugLineNum = 85;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 86;BA.debugLine="Up1.B4A_log=True";
_vvvvvv4.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 87;BA.debugLine="Up2.B4A_log=True";
_vvvvvv5.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 88;BA.debugLine="Up3.B4A_log=True";
_vvvvvv6.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 89;BA.debugLine="Up4.B4A_log=True";
_vvvvvv7.B4A_log = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 90;BA.debugLine="Up1.Initialize(\"Up1\")";
_vvvvvv4.Initialize(processBA,"Up1");
 //BA.debugLineNum = 91;BA.debugLine="Up2.Initialize(\"Up1\")";
_vvvvvv5.Initialize(processBA,"Up1");
 //BA.debugLineNum = 92;BA.debugLine="Up3.Initialize(\"Up1\")";
_vvvvvv6.Initialize(processBA,"Up1");
 //BA.debugLineNum = 93;BA.debugLine="Up4.Initialize(\"Up1\")";
_vvvvvv7.Initialize(processBA,"Up1");
 };
 //BA.debugLineNum = 96;BA.debugLine="adjuntandoFoto = False";
_vvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 107;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 108;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 109;BA.debugLine="If Msgbox2(\"Salir de la cámara?\", \"SALIR\", \"Si\"";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Salir de la cámara?"),BA.ObjectToCharSequence("SALIR"),"Si","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 110;BA.debugLine="camEx.StopPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ();
 //BA.debugLineNum = 111;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 112;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 113;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 114;BA.debugLine="Up1.UploadKill";
_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 115;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 116;BA.debugLine="Up3.UploadKill";
_vvvvvv6.UploadKill(processBA);
 //BA.debugLineNum = 117;BA.debugLine="Up4.UploadKill";
_vvvvvv7.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 119;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 122;BA.debugLine="If Msgbox2(\"Exit the camera?\", \"Leave\", \"Yes\",";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Exit the camera?"),BA.ObjectToCharSequence("Leave"),"Yes","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 123;BA.debugLine="camEx.StopPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ();
 //BA.debugLineNum = 124;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 125;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 126;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 127;BA.debugLine="Up1.UploadKill";
_vvvvvv4.UploadKill(processBA);
 //BA.debugLineNum = 128;BA.debugLine="Up2.UploadKill";
_vvvvvv5.UploadKill(processBA);
 //BA.debugLineNum = 129;BA.debugLine="Up3.UploadKill";
_vvvvvv6.UploadKill(processBA);
 //BA.debugLineNum = 130;BA.debugLine="Up4.UploadKill";
_vvvvvv7.UploadKill(processBA);
 }else {
 //BA.debugLineNum = 132;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 140;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 141;BA.debugLine="If camEx.IsInitialized = True Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.IsInitialized /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 142;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 };
 //BA.debugLineNum = 145;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 100;BA.debugLine="If adjuntandoFoto = False Then";
if (_vvvvv0==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 101;BA.debugLine="InitializeCamera";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 102;BA.debugLine="DesignaFoto";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 };
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static String  _btnadjuntarfoto_click() throws Exception{
 //BA.debugLineNum = 445;BA.debugLine="Sub btnAdjuntarFoto_Click";
 //BA.debugLineNum = 447;BA.debugLine="adjuntandoFoto = True";
_vvvvv0 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 450;BA.debugLine="camEx.StopPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ();
 //BA.debugLineNum = 451;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 452;BA.debugLine="DesignaFoto";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 453;BA.debugLine="CC.Initialize(\"CC\")";
_vvvvvv3.Initialize("CC");
 //BA.debugLineNum = 454;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 455;BA.debugLine="CC.Show(\"image/\", \"Elija la foto\")";
_vvvvvv3.Show(processBA,"image/","Elija la foto");
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 457;BA.debugLine="CC.Show(\"image/\", \"Choose the photo\")";
_vvvvvv3.Show(processBA,"image/","Choose the photo");
 };
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static String  _btnnofoto_click() throws Exception{
 //BA.debugLineNum = 515;BA.debugLine="Sub btnNoFoto_Click";
 //BA.debugLineNum = 516;BA.debugLine="imgFotoFinal.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 517;BA.debugLine="btnYesFoto.Visible = False";
mostCurrent._btnyesfoto.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 518;BA.debugLine="btnNoFoto.Visible = False";
mostCurrent._btnnofoto.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 521;BA.debugLine="InitializeCamera";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 522;BA.debugLine="DesignaFoto";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 523;BA.debugLine="End Sub";
return "";
}
public static String  _btntakepicture_click() throws Exception{
 //BA.debugLineNum = 323;BA.debugLine="Sub btnTakePicture_Click";
 //BA.debugLineNum = 325;BA.debugLine="If camEx.IsInitialized Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7.IsInitialized /*boolean*/ ()) { 
 //BA.debugLineNum = 329;BA.debugLine="btnTakePicture.Enabled = False";
mostCurrent._btntakepicture.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 330;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 331;BA.debugLine="ProgressDialogShow(\"Capturando foto\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Capturando foto"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 333;BA.debugLine="ProgressDialogShow(\"Capturing photo\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Capturing photo"));
 };
 //BA.debugLineNum = 335;BA.debugLine="camEx.TakePicture";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*String*/ ();
 };
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _btnyesfoto_click() throws Exception{
 //BA.debugLineNum = 509;BA.debugLine="Sub btnYesFoto_Click";
 //BA.debugLineNum = 510;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 511;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 513;BA.debugLine="End Sub";
return "";
}
public static String  _camera1_picturetaken(byte[] _data) throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 339;BA.debugLine="Sub Camera1_PictureTaken (Data() As Byte)";
 //BA.debugLineNum = 342;BA.debugLine="camEx.SavePictureToFile(Data, File.DirRootExterna";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv2 /*String*/ (_data,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+".jpg");
 //BA.debugLineNum = 345;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 346;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 347;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 348;BA.debugLine="If fotoNombreDestino.EndsWith(\"_1\") Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.endsWith("_1")) { 
 //BA.debugLineNum = 349;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3),_map1);
 //BA.debugLineNum = 350;BA.debugLine="Main.fotopath0 = fotoNombreDestino";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv3 /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3;
 }else if(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.endsWith("_2")) { 
 //BA.debugLineNum = 352;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_loc";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3),_map1);
 //BA.debugLineNum = 353;BA.debugLine="Main.fotopath1 = fotoNombreDestino";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv4 /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3;
 };
 //BA.debugLineNum = 357;BA.debugLine="camEx.StopPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ();
 //BA.debugLineNum = 358;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 361;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 364;BA.debugLine="imgFotoFinal.Initialize(\"imgFotoFinal\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.Initialize(mostCurrent.activityBA,"imgFotoFinal");
 //BA.debugLineNum = 365;BA.debugLine="imgFotoFinal.Bitmap = LoadBitmapResize(File.DirRo";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmapResize(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+".jpg",mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight(),anywheresoftware.b4a.keywords.Common.True).getObject()));
 //BA.debugLineNum = 366;BA.debugLine="Activity.AddView(imgFotoFinal, 0,0,Activity.Width";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getObject()),(int) (0),(int) (0),mostCurrent._activity.getWidth(),mostCurrent._activity.getHeight());
 //BA.debugLineNum = 368;BA.debugLine="btnYesFoto.Visible = True";
mostCurrent._btnyesfoto.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 369;BA.debugLine="btnNoFoto.Visible= True";
mostCurrent._btnnofoto.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 370;BA.debugLine="btnNoFoto.BringToFront";
mostCurrent._btnnofoto.BringToFront();
 //BA.debugLineNum = 371;BA.debugLine="btnYesFoto.BringToFront";
mostCurrent._btnyesfoto.BringToFront();
 //BA.debugLineNum = 374;BA.debugLine="btnAdjuntarFoto.visible = False";
mostCurrent._btnadjuntarfoto.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 400;BA.debugLine="End Sub";
return "";
}
public static String  _camera1_ready(boolean _success) throws Exception{
 //BA.debugLineNum = 280;BA.debugLine="Sub Camera1_Ready (Success As Boolean)";
 //BA.debugLineNum = 281;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 282;BA.debugLine="Log(camEx.GetSupportedPicturesSizes)";
anywheresoftware.b4a.keywords.Common.LogImpl("514417922",BA.ObjectToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvv3 /*caza.mosquito.cameraexclass._camerasize[]*/ ()),0);
 //BA.debugLineNum = 283;BA.debugLine="Log(camEx.GetPictureSize)";
anywheresoftware.b4a.keywords.Common.LogImpl("514417923",BA.ObjectToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvv4 /*caza.mosquito.cameraexclass._camerasize*/ ()),0);
 //BA.debugLineNum = 284;BA.debugLine="SetMaxSize";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4();
 //BA.debugLineNum = 285;BA.debugLine="camEx.SetContinuousAutoFocus";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv4 /*String*/ ();
 //BA.debugLineNum = 286;BA.debugLine="camEx.CommitParameters";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 287;BA.debugLine="camEx.StartPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 288;BA.debugLine="Log(camEx.GetPreviewSize)";
anywheresoftware.b4a.keywords.Common.LogImpl("514417928",BA.ObjectToString(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvv6 /*caza.mosquito.cameraexclass._camerasize*/ ()),0);
 }else {
 //BA.debugLineNum = 290;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 291;BA.debugLine="ToastMessageShow(\"No se puede encender la cámar";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No se puede encender la cámara. Revise los permisos de la aplicación"),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 293;BA.debugLine="ToastMessageShow(\"Can't start the camera, check";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Can't start the camera, check the permissions"),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 299;BA.debugLine="End Sub";
return "";
}
public static String  _cc_result(boolean _success,String _dir,String _filename) throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 460;BA.debugLine="Sub CC_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 462;BA.debugLine="If Success = True Then";
if (_success==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 465;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 466;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 467;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 468;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 469;BA.debugLine="If fotoNombreDestino.EndsWith(\"_1\") Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.endsWith("_1")) { 
 //BA.debugLineNum = 470;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal &";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+".jpg");
 //BA.debugLineNum = 471;BA.debugLine="Main.fotopath0 = fotoNombreDestino";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv3 /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3;
 //BA.debugLineNum = 472;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto1",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3),_map1);
 }else if(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.endsWith("_2")) { 
 //BA.debugLineNum = 474;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal &";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/CazaMosquitos/",mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3+".jpg");
 //BA.debugLineNum = 475;BA.debugLine="Main.fotopath1 = fotoNombreDestino";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv4 /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3;
 //BA.debugLineNum = 476;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","foto2",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3),_map1);
 };
 //BA.debugLineNum = 479;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 480;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 484;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 485;BA.debugLine="ToastMessageShow(\"Hubo un problema adjuntando l";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Hubo un problema adjuntando la foto"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 487;BA.debugLine="ToastMessageShow(\"There was a problem attaching";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("There was a problem attaching the photo"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 489;BA.debugLine="imgFotoFinal.RemoveView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.RemoveView();
 //BA.debugLineNum = 490;BA.debugLine="btnYesFoto.Visible = False";
mostCurrent._btnyesfoto.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 491;BA.debugLine="btnNoFoto.Visible = False";
mostCurrent._btnnofoto.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 492;BA.debugLine="adjuntandoFoto = False";
_vvvvv0 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 494;BA.debugLine="InitializeCamera";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0();
 //BA.debugLineNum = 495;BA.debugLine="DesignaFoto";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 };
 //BA.debugLineNum = 499;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1() throws Exception{
 //BA.debugLineNum = 188;BA.debugLine="Sub DesignaFoto";
 //BA.debugLineNum = 190;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 191;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 192;BA.debugLine="ProgressDialogShow(\"Preparando cámara...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Preparando cámara..."));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 194;BA.debugLine="ProgressDialogShow(\"Preparing camera...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,BA.ObjectToCharSequence("Preparing camera..."));
 };
 //BA.debugLineNum = 201;BA.debugLine="If currentFoto = \"dorsal\" Then";
if ((_vvvvvv0).equals("dorsal")) { 
 //BA.debugLineNum = 202;BA.debugLine="fotoNombreDestino = frmprincipal.fullidcurrentpr";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = mostCurrent._vvvvvvvvvvvvvvvvvv3._v5 /*String*/ +"_1";
 //BA.debugLineNum = 203;BA.debugLine="fotonumlibre = 1";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = (int) (1);
 }else if((_vvvvvv0).equals("ventral")) { 
 //BA.debugLineNum = 205;BA.debugLine="fotoNombreDestino = frmprincipal.fullidcurrentpr";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = mostCurrent._vvvvvvvvvvvvvvvvvv3._v5 /*String*/ +"_2";
 //BA.debugLineNum = 206;BA.debugLine="fotonumlibre = 2";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = (int) (2);
 };
 //BA.debugLineNum = 211;BA.debugLine="Panel1.Visible = True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 213;BA.debugLine="imgTemplate.Visible = False";
mostCurrent._imgtemplate.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 255;BA.debugLine="btnTakePicture.Visible = True";
mostCurrent._btntakepicture.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 256;BA.debugLine="btnTakePicture.Enabled = True";
mostCurrent._btntakepicture.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 259;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 29;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private camEx As CameraExClass";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = new caza.mosquito.cameraexclass();
 //BA.debugLineNum = 31;BA.debugLine="Private btnTakePicture As Button";
mostCurrent._btntakepicture = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private imgFlash As ImageView";
mostCurrent._imgflash = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private btnAdjuntarFoto As Button";
mostCurrent._btnadjuntarfoto = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private imgMenu As Label";
mostCurrent._imgmenu = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private seekFocus As SeekBar";
mostCurrent._seekfocus = new anywheresoftware.b4a.objects.SeekBarWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Dim foto1,foto2,foto3,foto4 As String";
mostCurrent._vvvvvvv0 = "";
mostCurrent._vvvvvvvv1 = "";
mostCurrent._vvvvvvvv2 = "";
mostCurrent._vvvvvvvv3 = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim fotoNombreDestino As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 40;BA.debugLine="Dim lblTemplate As Label";
mostCurrent._lbltemplate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim imgTemplate As ImageView";
mostCurrent._imgtemplate = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Dim newfilename As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim fotonumlibre As Int";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = 0;
 //BA.debugLineNum = 51;BA.debugLine="Dim imgFotoFinal As ImageView";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private btnNoFoto As Button";
mostCurrent._btnnofoto = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private btnYesFoto As Button";
mostCurrent._btnyesfoto = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _imgflash_click() throws Exception{
float[] _f = null;
anywheresoftware.b4a.objects.collections.List _flashmodes = null;
String _flash = "";
 //BA.debugLineNum = 410;BA.debugLine="Sub imgFlash_Click";
 //BA.debugLineNum = 411;BA.debugLine="Dim f() As Float = camEx.GetFocusDistances";
_f = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvv7 /*float[]*/ ();
 //BA.debugLineNum = 412;BA.debugLine="Log(f(0) & \", \" & f(1) & \", \" & f(2))";
anywheresoftware.b4a.keywords.Common.LogImpl("514680066",BA.NumberToString(_f[(int) (0)])+", "+BA.NumberToString(_f[(int) (1)])+", "+BA.NumberToString(_f[(int) (2)]),0);
 //BA.debugLineNum = 413;BA.debugLine="Dim flashModes As List = camEx.GetSupportedFlashM";
_flashmodes = new anywheresoftware.b4a.objects.collections.List();
_flashmodes = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvv0 /*anywheresoftware.b4a.objects.collections.List*/ ();
 //BA.debugLineNum = 414;BA.debugLine="If flashModes.IsInitialized = False Then";
if (_flashmodes.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 415;BA.debugLine="ToastMessageShow(\"Flash not supported.\", False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Flash not supported."),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 416;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 418;BA.debugLine="Dim flash As String = flashModes.Get((flashModes.";
_flash = BA.ObjectToString(_flashmodes.Get((int) ((_flashmodes.IndexOf((Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ()))+1)%_flashmodes.getSize())));
 //BA.debugLineNum = 419;BA.debugLine="camEx.SetFlashMode(flash)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv0 /*String*/ (_flash);
 //BA.debugLineNum = 420;BA.debugLine="ToastMessageShow(flash, False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_flash),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 421;BA.debugLine="camEx.CommitParameters";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 422;BA.debugLine="End Sub";
return "";
}
public static String  _imgmenu_click() throws Exception{
 //BA.debugLineNum = 155;BA.debugLine="Sub imgMenu_Click";
 //BA.debugLineNum = 156;BA.debugLine="Activity.OpenMenu";
mostCurrent._activity.OpenMenu();
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0() throws Exception{
 //BA.debugLineNum = 271;BA.debugLine="Private Sub InitializeCamera";
 //BA.debugLineNum = 273;BA.debugLine="camEx.Initialize(Panel1, frontCamera, Me, \"Camera";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._initialize /*String*/ (mostCurrent.activityBA,mostCurrent._panel1,_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7,frmcamara.getObject(),"Camera1");
 //BA.debugLineNum = 276;BA.debugLine="frontCamera = camEx.Front";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*boolean*/ ;
 //BA.debugLineNum = 278;BA.debugLine="btnAdjuntarFoto.visible = True";
mostCurrent._btnadjuntarfoto.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 279;BA.debugLine="End Sub";
return "";
}
public static String  _mnuadjuntar_click() throws Exception{
 //BA.debugLineNum = 161;BA.debugLine="Sub mnuAdjuntar_Click";
 //BA.debugLineNum = 163;BA.debugLine="adjuntandoFoto = True";
_vvvvv0 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 166;BA.debugLine="camEx.StopPreview";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ();
 //BA.debugLineNum = 167;BA.debugLine="camEx.Release";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 168;BA.debugLine="DesignaFoto";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 //BA.debugLineNum = 169;BA.debugLine="CC.Initialize(\"CC\")";
_vvvvvv3.Initialize("CC");
 //BA.debugLineNum = 171;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 172;BA.debugLine="CC.Show(\"image/\", \"Elija la foto\")";
_vvvvvv3.Show(processBA,"image/","Elija la foto");
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 174;BA.debugLine="CC.Show(\"image/\", \"Choose the photo\")";
_vvvvvv3.Show(processBA,"image/","Choose the photo");
 };
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _mnuconsejos_click() throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Sub mnuConsejos_Click";
 //BA.debugLineNum = 159;BA.debugLine="StartActivity(frmComoFotos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv5.getObject()));
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 8;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim adjuntandoFoto As Boolean";
_vvvvv0 = false;
 //BA.debugLineNum = 10;BA.debugLine="Dim hc As OkHttpClient";
_vvvvvv1 = new anywheresoftware.b4h.okhttp.OkHttpClientWrapper();
 //BA.debugLineNum = 11;BA.debugLine="Dim out As OutputStream";
_vvvvvv2 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
 //BA.debugLineNum = 13;BA.debugLine="Private frontCamera As Boolean = False";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 14;BA.debugLine="Dim CC As ContentChooser";
_vvvvvv3 = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 17;BA.debugLine="Dim Up1 As UploadFilePhp";
_vvvvvv4 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 18;BA.debugLine="Dim Up2 As UploadFilePhp";
_vvvvvv5 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 19;BA.debugLine="Dim Up3 As UploadFilePhp";
_vvvvvv6 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 20;BA.debugLine="Dim Up4 As UploadFilePhp";
_vvvvvv7 = new com.spinter.uploadfilephp.UploadFilePhp();
 //BA.debugLineNum = 23;BA.debugLine="Dim currentFoto As String";
_vvvvvv0 = "";
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _seekfocus_valuechanged(int _value,boolean _userchanged) throws Exception{
 //BA.debugLineNum = 431;BA.debugLine="Sub seekFocus_ValueChanged (Value As Int, UserChan";
 //BA.debugLineNum = 432;BA.debugLine="If UserChanged = False Or camEx.IsZoomSupported =";
if (_userchanged==anywheresoftware.b4a.keywords.Common.False || mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvv7 /*boolean*/ ()==anywheresoftware.b4a.keywords.Common.False) { 
if (true) return "";};
 //BA.debugLineNum = 433;BA.debugLine="camEx.Zoom = Value / 100 * camEx.GetMaxZoom";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._setvvvvvvvvvvvvvvvvvvvvvvvvvvv0 /*int*/ ((int) (_value/(double)100*mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvv1 /*int*/ ()));
 //BA.debugLineNum = 434;BA.debugLine="camEx.CommitParameters";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 435;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4() throws Exception{
caza.mosquito.cameraexclass._camerasize _mincs = null;
caza.mosquito.cameraexclass._camerasize _cs = null;
 //BA.debugLineNum = 300;BA.debugLine="Private Sub SetMaxSize";
 //BA.debugLineNum = 301;BA.debugLine="Dim minCS As CameraSize";
_mincs = new caza.mosquito.cameraexclass._camerasize();
 //BA.debugLineNum = 302;BA.debugLine="For Each cs As CameraSize In camEx.GetSupportedPi";
{
final caza.mosquito.cameraexclass._camerasize[] group2 = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvv3 /*caza.mosquito.cameraexclass._camerasize[]*/ ();
final int groupLen2 = group2.length
;int index2 = 0;
;
for (; index2 < groupLen2;index2++){
_cs = group2[index2];
 //BA.debugLineNum = 303;BA.debugLine="If minCS.Width = 0 Then";
if (_mincs.Width /*int*/ ==0) { 
 //BA.debugLineNum = 304;BA.debugLine="minCS = cs";
_mincs = _cs;
 }else {
 //BA.debugLineNum = 307;BA.debugLine="If Power(minCS.Width, 2) + Power(minCS.Height,";
if (anywheresoftware.b4a.keywords.Common.Power(_mincs.Width /*int*/ ,2)+anywheresoftware.b4a.keywords.Common.Power(_mincs.Height /*int*/ ,2)<anywheresoftware.b4a.keywords.Common.Power(_cs.Width /*int*/ ,2)+anywheresoftware.b4a.keywords.Common.Power(_cs.Height /*int*/ ,2)) { 
 //BA.debugLineNum = 308;BA.debugLine="minCS = cs";
_mincs = _cs;
 };
 };
 }
};
 //BA.debugLineNum = 312;BA.debugLine="camEx.SetPictureSize(minCS.Width, minCS.Height)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvvvvvvvvvvvvvvvvvvv4 /*String*/ (_mincs.Width /*int*/ ,_mincs.Height /*int*/ );
 //BA.debugLineNum = 313;BA.debugLine="Log(\"Selected size: \" & minCS)";
anywheresoftware.b4a.keywords.Common.LogImpl("514483469","Selected size: "+BA.ObjectToString(_mincs),0);
 //BA.debugLineNum = 314;BA.debugLine="End Sub";
return "";
}
}
