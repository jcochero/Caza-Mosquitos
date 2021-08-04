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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static String _vvvvvvvvvv1 = "";
public static String _vvvvvvvvvv2 = "";
public static int _vvvvvvvvvv3 = 0;
public static String _vvvvvvvvvv4 = "";
public static String _vvvvvvvvvv5 = "";
public static String _vvvvvvvvvv6 = "";
public static String _vvvvvvvvvv7 = "";
public static String _vvvvvvvvvv0 = "";
public static String _vvvvvvvvvvv1 = "";
public static String _vvvvvvvvvvv2 = "";
public static String _vvvvvvvvvvv3 = "";
public static String _vvvvvvvvvvv4 = "";
public static String _vvvvvvvvvvv5 = "";
public static String _vvvvvvvvvvv6 = "";
public static String _vvvvvvvvvvv7 = "";
public static String _vvvvvvvvvvv0 = "";
public static String _vvvvvvvvvvvv1 = "";
public static String _vvvvvvvvvvvv2 = "";
public static String _vvvvvvvvvvvv3 = "";
public static String _vvvvvvvvvvvv4 = "";
public static String _vvvvvvvvvvvv5 = "";
public static String _vvvvvvvvvvvv6 = "";
public static String _vvvvvvvvvvvv7 = "";
public static String _vvvvvvvvvvvv0 = "";
public static boolean _vvvvvvvvvvvvv1 = false;
public static boolean _vvvvvvvvvvvvv2 = false;
public static anywheresoftware.b4a.phone.Phone _vv0 = null;
public static String _v7 = "";
public static String _vvvvvvvvvvvvv3 = "";
public static String _vvvvvvvvvvvvv4 = "";
public static boolean _vvvvvvvvvvvvv5 = false;
public static String _vvvvvvvvvvvvv6 = "";
public static String _vvvvvvvvvvvvv7 = "";
public static String _vvvvvvvvvvvvv0 = "";
public static String _vvvvvvvvvvvvvv1 = "";
public static String _vvvvvvvvvvvvvv2 = "";
public static String _vvvvvvvvvvvvvv3 = "";
public static anywheresoftware.b4a.objects.collections.List _vvvvvvvvvvvvvv4 = null;
public static anywheresoftware.b4a.objects.RuntimePermissions _v6 = null;
public static String _vvvvvvvvvvvvvv5 = "";
public static anywheresoftware.b4a.objects.Timer _vvvvvvv1 = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
public anywheresoftware.b4a.objects.ProgressBarWrapper _pgbload = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblestado = null;
public anywheresoftware.b4a.objects.LabelWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnoffline = null;
public anywheresoftware.b4a.sql.SQL.CursorWrapper _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = null;
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

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (frmprincipal.mostCurrent != null);
vis = vis | (frmcomofotos.mostCurrent != null);
vis = vis | (frmlogin.mostCurrent != null);
vis = vis | (frmabout.mostCurrent != null);
vis = vis | (frmaprender.mostCurrent != null);
vis = vis | (frmcamara.mostCurrent != null);
vis = vis | (frmcomotransmiten.mostCurrent != null);
vis = vis | (frmcomovemos_app.mostCurrent != null);
vis = vis | (frmcomovemos_enfermedades.mostCurrent != null);
vis = vis | (frmcomovemos_porqueexiste.mostCurrent != null);
vis = vis | (frmdatosanteriores.mostCurrent != null);
vis = vis | (frmdondecria.mostCurrent != null);
vis = vis | (frmeditprofile.mostCurrent != null);
vis = vis | (frmelmosquito.mostCurrent != null);
vis = vis | (frmenfermedades.mostCurrent != null);
vis = vis | (frmfotos.mostCurrent != null);
vis = vis | (frmfotoscriadero.mostCurrent != null);
vis = vis | (frmidentificarmosquito.mostCurrent != null);
vis = vis | (frminfografias_main.mostCurrent != null);
vis = vis | (frminstrucciones.mostCurrent != null);
vis = vis | (frmlocalizacion.mostCurrent != null);
vis = vis | (frmmapa.mostCurrent != null);
vis = vis | (frmpoliticadatos.mostCurrent != null);
vis = vis | (frmquehacer.mostCurrent != null);
vis = vis | (register.mostCurrent != null);
return vis;}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(caza.mosquito.main parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
caza.mosquito.main parent;
boolean _firsttime;
anywheresoftware.b4a.phone.Phone _p = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 108;BA.debugLine="Dim p As Phone";
_p = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 109;BA.debugLine="p.SetScreenOrientation(1)";
_p.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 111;BA.debugLine="If Starter.auth.CurrentUser.IsInitialized Then St";
if (true) break;

case 1:
//if
this.state = 6;
if (parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._vv4 /*anywheresoftware.b4a.objects.FirebaseAuthWrapper*/ .getCurrentUser().IsInitialized()) { 
this.state = 3;
;}if (true) break;

case 3:
//C
this.state = 6;
parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._vv4 /*anywheresoftware.b4a.objects.FirebaseAuthWrapper*/ .SignOutFromGoogle();
if (true) break;

case 6:
//C
this.state = 7;
;
 //BA.debugLineNum = 114;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 13;
return;
case 13:
//C
this.state = 7;
;
 //BA.debugLineNum = 115;BA.debugLine="deviceID = Starter.UpdateFCMToken";
parent._vvvvvvvvvvvvvv1 = parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._vv7 /*String*/ ();
 //BA.debugLineNum = 126;BA.debugLine="Starter.dbdir = DBUtils.CopyDBFromAssets(\"databas";
parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._vv3 /*String*/  = parent.mostCurrent._vvvvvvvvvvvvvvvvvv7._vvv2 /*String*/ (mostCurrent.activityBA,"database.db");
 //BA.debugLineNum = 127;BA.debugLine="Starter.speciesDBDir = DBUtils.CopyDBFromAssets(\"";
parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._vv2 /*String*/  = parent.mostCurrent._vvvvvvvvvvvvvvvvvv7._vvv2 /*String*/ (mostCurrent.activityBA,"speciesdb.db");
 //BA.debugLineNum = 131;BA.debugLine="BuscarConfiguracion";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4();
 //BA.debugLineNum = 133;BA.debugLine="Activity.LoadLayout(\"layload\")";
parent.mostCurrent._activity.LoadLayout("layload",mostCurrent.activityBA);
 //BA.debugLineNum = 134;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
parent.mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(parent.mostCurrent._activity.getObject())));
 //BA.debugLineNum = 136;BA.debugLine="deviceID = utilidades.GetDeviceId";
parent._vvvvvvvvvvvvvv1 = parent.mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv5 /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 138;BA.debugLine="If lang = \"es\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent._vvvvvvvvvvvvvv5).equals("es")) { 
this.state = 9;
}else if((parent._vvvvvvvvvvvvvv5).equals("en")) { 
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 139;BA.debugLine="lblEstado.Text = \"Comprobando conexión a interne";
parent.mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Comprobando conexión a internet"));
 //BA.debugLineNum = 140;BA.debugLine="btnOffline.Text = \"Trabajar sin conexión\"";
parent.mostCurrent._btnoffline.setText(BA.ObjectToCharSequence("Trabajar sin conexión"));
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 142;BA.debugLine="lblEstado.Text = \"Checking the connection to the";
parent.mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Checking the connection to the internet"));
 //BA.debugLineNum = 143;BA.debugLine="btnOffline.Text = \"Work offline\"";
parent.mostCurrent._btnoffline.setText(BA.ObjectToCharSequence("Work offline"));
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 145;BA.debugLine="timer1.Initialize(\"Timer1\", 1000)";
parent._vvvvvvv1.Initialize(processBA,"Timer1",(long) (1000));
 //BA.debugLineNum = 146;BA.debugLine="timer1.Enabled = True";
parent._vvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 245;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 246;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 247;BA.debugLine="If Msgbox2(\"Salir de la aplicación?\", \"SALIR\",";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Salir de la aplicación?"),BA.ObjectToCharSequence("SALIR"),"Si","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 248;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 249;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 }else {
 //BA.debugLineNum = 251;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 254;BA.debugLine="If Msgbox2(\"Exit the app?\", \"EXIT\", \"YES\", \"\",";
if (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Exit the app?"),BA.ObjectToCharSequence("EXIT"),"YES","","No",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 255;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 256;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 }else {
 //BA.debugLineNum = 258;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 };
 //BA.debugLineNum = 263;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 309;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 311;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(caza.mosquito.main parent) {
this.parent = parent;
}
caza.mosquito.main parent;
String _permission = "";
boolean _result = false;
Object[] group1;
int index1;
int groupLen1;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 267;BA.debugLine="For Each Permission As String In Array(rp.PERMISS";
if (true) break;

case 1:
//for
this.state = 14;
group1 = new Object[]{(Object)(parent._v6.PERMISSION_ACCESS_FINE_LOCATION),(Object)(parent._v6.PERMISSION_CAMERA),(Object)(parent._v6.PERMISSION_WRITE_EXTERNAL_STORAGE)};
index1 = 0;
groupLen1 = group1.length;
this.state = 26;
if (true) break;

case 26:
//C
this.state = 14;
if (index1 < groupLen1) {
this.state = 3;
_permission = BA.ObjectToString(group1[index1]);}
if (true) break;

case 27:
//C
this.state = 26;
index1++;
if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 268;BA.debugLine="Sleep(100)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (100));
this.state = 28;
return;
case 28:
//C
this.state = 4;
;
 //BA.debugLineNum = 269;BA.debugLine="rp.CheckAndRequest(Permission)";
parent._v6.CheckAndRequest(processBA,_permission);
 //BA.debugLineNum = 270;BA.debugLine="Wait For Activity_PermissionResult (Permission A";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 29;
return;
case 29:
//C
this.state = 4;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 271;BA.debugLine="If Result = False Then";
if (true) break;

case 4:
//if
this.state = 13;
if (_result==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 6;
}if (true) break;

case 6:
//C
this.state = 7;
 //BA.debugLineNum = 272;BA.debugLine="If lang = \"es\" Then";
if (true) break;

case 7:
//if
this.state = 12;
if ((parent._vvvvvvvvvvvvvv5).equals("es")) { 
this.state = 9;
}else if((parent._vvvvvvvvvvvvvv5).equals("en")) { 
this.state = 11;
}if (true) break;

case 9:
//C
this.state = 12;
 //BA.debugLineNum = 273;BA.debugLine="ToastMessageShow(\"Para usar Caza Mosquitos, se";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Para usar Caza Mosquitos, se necesitan permisos para usar la cámara, el GPS y de escritura"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 11:
//C
this.state = 12;
 //BA.debugLineNum = 275;BA.debugLine="ToastMessageShow(\"To use Caza Mosquitos, permi";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("To use Caza Mosquitos, permissions to use the camera, the GPS and to write data are needed"),anywheresoftware.b4a.keywords.Common.True);
 if (true) break;

case 12:
//C
this.state = 13;
;
 //BA.debugLineNum = 278;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 279;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 13:
//C
this.state = 27;
;
 if (true) break;
if (true) break;
;
 //BA.debugLineNum = 286;BA.debugLine="If File.ExternalWritable = True Then";

case 14:
//if
this.state = 19;
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 16;
}else {
this.state = 18;
}if (true) break;

case 16:
//C
this.state = 19;
 //BA.debugLineNum = 287;BA.debugLine="Starter.savedir = File.DirRootExternal";
parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._v7 /*String*/  = anywheresoftware.b4a.keywords.Common.File.getDirRootExternal();
 if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 289;BA.debugLine="Starter.savedir = File.DirInternal";
parent.mostCurrent._vvvvvvvvvvvvvvvvvv4._v7 /*String*/  = anywheresoftware.b4a.keywords.Common.File.getDirInternal();
 if (true) break;
;
 //BA.debugLineNum = 296;BA.debugLine="If File.Exists(savedir & \"/CazaMosquitos/\", \"\") =";

case 19:
//if
this.state = 22;
if (anywheresoftware.b4a.keywords.Common.File.Exists(parent._v7+"/CazaMosquitos/","")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 21;
}if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 297;BA.debugLine="File.MakeDir(savedir, \"CazaMosquitos\")";
anywheresoftware.b4a.keywords.Common.File.MakeDir(parent._v7,"CazaMosquitos");
 if (true) break;
;
 //BA.debugLineNum = 303;BA.debugLine="If File.Exists(savedir & \"/CazaMosquitos/sent/\",";

case 22:
//if
this.state = 25;
if (anywheresoftware.b4a.keywords.Common.File.Exists(parent._v7+"/CazaMosquitos/sent/","")==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 304;BA.debugLine="File.MakeDir(savedir & \"/CazaMosquitos/\", \"sent\"";
anywheresoftware.b4a.keywords.Common.File.MakeDir(parent._v7+"/CazaMosquitos/","sent");
 if (true) break;

case 25:
//C
this.state = -1;
;
 //BA.debugLineNum = 308;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static String  _btnoffline_click() throws Exception{
 //BA.debugLineNum = 584;BA.debugLine="Sub btnOffline_Click";
 //BA.debugLineNum = 586;BA.debugLine="CallSubDelayed2(DownloadService, \"CancelDownload\"";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"CancelDownload",(Object)(_vvvvvvvvvvvvvv2+"/connect2/connecttest.php"));
 //BA.debugLineNum = 588;BA.debugLine="modooffline = True";
_vvvvvvvvvvvvv1 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 589;BA.debugLine="forceoffline = True";
_vvvvvvvvvvvvv2 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 590;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 591;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 592;BA.debugLine="utilidades.Mensaje(\"Advertencia\", \"MsgIcon.png\",";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Advertencia","MsgIcon.png","Modo sin internet","Has seleccionado el modo sin conexión. Caza Mosquitos iniciará con el último usuario que utilizaste, pero no podrás ver ni cargar puntos hasta que no te conectes.",(Object)("OK"),(Object)(""),(Object)(""),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 594;BA.debugLine="utilidades.Mensaje(\"Warning\", \"MsgIcon.png\", \"Of";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Warning","MsgIcon.png","Offline mode","You have selected the offline mode. Caza Mosquitos will start with the last user that logged in, but won't be able to send data.",(Object)("OK"),(Object)(""),(Object)(""),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 598;BA.debugLine="CargarBienvenidos_Nuevo";
_cargarbienvenidos_nuevo();
 //BA.debugLineNum = 600;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4() throws Exception{
anywheresoftware.b4a.objects.collections.Map _configmap = null;
anywheresoftware.b4a.objects.collections.List _newconfig = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
String _deviceactual = "";
anywheresoftware.b4a.objects.collections.Map _usermap = null;
 //BA.debugLineNum = 155;BA.debugLine="Sub BuscarConfiguracion";
 //BA.debugLineNum = 157;BA.debugLine="Starter.sqlDB.Initialize(Starter.dbdir, \"database";
mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ .Initialize(mostCurrent._vvvvvvvvvvvvvvvvvv4._vv3 /*String*/ ,"database.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 158;BA.debugLine="Starter.speciesDB.Initialize(Starter.speciesDBDir";
mostCurrent._vvvvvvvvvvvvvvvvvv4._vv1 /*anywheresoftware.b4a.sql.SQL*/ .Initialize(mostCurrent._vvvvvvvvvvvvvvvvvv4._vv2 /*String*/ ,"speciesdb.db",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 162;BA.debugLine="Dim configmap As Map";
_configmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 163;BA.debugLine="configmap.Initialize";
_configmap.Initialize();
 //BA.debugLineNum = 164;BA.debugLine="configmap = DBUtils.ExecuteMap(Starter.sqlDB, \"SE";
_configmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM appconfig",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 165;BA.debugLine="If configmap = Null Or configmap.IsInitialized =";
if (_configmap== null || _configmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 166;BA.debugLine="Dim newconfig As List";
_newconfig = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 167;BA.debugLine="newconfig.Initialize";
_newconfig.Initialize();
 //BA.debugLineNum = 168;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 169;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 171;BA.debugLine="versionactual = Application.VersionCode";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Application.getVersionCode());
 //BA.debugLineNum = 173;BA.debugLine="deviceID = utilidades.GetDeviceId";
_vvvvvvvvvvvvvv1 = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv5 /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 174;BA.debugLine="m.Put(\"configID\", \"1\")";
_m.Put((Object)("configID"),(Object)("1"));
 //BA.debugLineNum = 175;BA.debugLine="m.Put(\"appVersion\", versionactual)";
_m.Put((Object)("appVersion"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5));
 //BA.debugLineNum = 176;BA.debugLine="m.Put(\"firstTime\", \"True\")";
_m.Put((Object)("firstTime"),(Object)("True"));
 //BA.debugLineNum = 177;BA.debugLine="m.Put(\"deviceID\", deviceID)";
_m.Put((Object)("deviceID"),(Object)(_vvvvvvvvvvvvvv1));
 //BA.debugLineNum = 178;BA.debugLine="m.Put(\"applang\", \"es\")";
_m.Put((Object)("applang"),(Object)("es"));
 //BA.debugLineNum = 180;BA.debugLine="lang = \"es\"";
_vvvvvvvvvvvvvv5 = "es";
 //BA.debugLineNum = 181;BA.debugLine="newconfig.Add(m)";
_newconfig.Add((Object)(_m.getObject()));
 //BA.debugLineNum = 182;BA.debugLine="DBUtils.InsertMaps(Starter.sqlDB,\"appconfig\", ne";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv1 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"appconfig",_newconfig);
 }else {
 //BA.debugLineNum = 185;BA.debugLine="Dim configmap As Map";
_configmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 186;BA.debugLine="configmap.Initialize";
_configmap.Initialize();
 //BA.debugLineNum = 187;BA.debugLine="configmap = DBUtils.ExecuteMap(Starter.sqlDB, \"S";
_configmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM appconfig WHERE configID = '1'",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 188;BA.debugLine="If configmap = Null Or configmap.IsInitialized =";
if (_configmap== null || _configmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 189;BA.debugLine="deviceID = \"noID\"";
_vvvvvvvvvvvvvv1 = "noID";
 //BA.debugLineNum = 190;BA.debugLine="lang = \"es\"";
_vvvvvvvvvvvvvv5 = "es";
 }else {
 //BA.debugLineNum = 192;BA.debugLine="deviceID = configmap.Get(\"deviceID\")";
_vvvvvvvvvvvvvv1 = BA.ObjectToString(_configmap.Get((Object)("deviceID")));
 //BA.debugLineNum = 193;BA.debugLine="lang = configmap.Get(\"applang\")";
_vvvvvvvvvvvvvv5 = BA.ObjectToString(_configmap.Get((Object)("applang")));
 };
 };
 //BA.debugLineNum = 200;BA.debugLine="Dim deviceactual As String";
_deviceactual = "";
 //BA.debugLineNum = 201;BA.debugLine="deviceactual = utilidades.GetDeviceId";
_deviceactual = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv5 /*String*/ (mostCurrent.activityBA);
 //BA.debugLineNum = 202;BA.debugLine="If deviceactual <> deviceID Then";
if ((_deviceactual).equals(_vvvvvvvvvvvvvv1) == false) { 
 //BA.debugLineNum = 203;BA.debugLine="deviceID = deviceactual";
_vvvvvvvvvvvvvv1 = _deviceactual;
 };
 //BA.debugLineNum = 208;BA.debugLine="Dim userMap As Map";
_usermap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 209;BA.debugLine="userMap.Initialize";
_usermap.Initialize();
 //BA.debugLineNum = 210;BA.debugLine="userMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SELE";
_usermap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM userconfig WHERE lastUser = 'si'",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 211;BA.debugLine="If userMap = Null Or userMap.IsInitialized = Fals";
if (_usermap== null || _usermap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 212;BA.debugLine="username = \"None\"";
_vvvvvvvvvvvv7 = "None";
 //BA.debugLineNum = 213;BA.debugLine="pass = \"None\"";
_vvvvvvvvvvvv0 = "None";
 }else {
 //BA.debugLineNum = 215;BA.debugLine="username = userMap.Get(\"username\")";
_vvvvvvvvvvvv7 = BA.ObjectToString(_usermap.Get((Object)("username")));
 //BA.debugLineNum = 216;BA.debugLine="pass = userMap.Get(\"pass\")";
_vvvvvvvvvvvv0 = BA.ObjectToString(_usermap.Get((Object)("pass")));
 //BA.debugLineNum = 217;BA.debugLine="strUserLocation = userMap.Get(\"userlocation\")";
_vvvvvvvvvvvv1 = BA.ObjectToString(_usermap.Get((Object)("userlocation")));
 //BA.debugLineNum = 218;BA.debugLine="strUserFullName = userMap.Get(\"userfullname\")";
_vvvvvvvvvvvv3 = BA.ObjectToString(_usermap.Get((Object)("userfullname")));
 //BA.debugLineNum = 219;BA.debugLine="strUserOrg = userMap.Get(\"userorg\")";
_vvvvvvvvvvvv4 = BA.ObjectToString(_usermap.Get((Object)("userorg")));
 //BA.debugLineNum = 220;BA.debugLine="strUserTipoUsuario = userMap.Get(\"usertipousuari";
_vvvvvvvvvvvv5 = BA.ObjectToString(_usermap.Get((Object)("usertipousuario")));
 //BA.debugLineNum = 221;BA.debugLine="strUserGroup = userMap.Get(\"usergroup\")";
_vvvvvvvvvvvv6 = BA.ObjectToString(_usermap.Get((Object)("usergroup")));
 };
 //BA.debugLineNum = 227;BA.debugLine="speciesMap.Initialize";
_vvvvvvvvvvvvvv4.Initialize();
 //BA.debugLineNum = 228;BA.debugLine="speciesMap = DBUtils.ExecuteMemoryTable(Starter.s";
_vvvvvvvvvvvvvv4 = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv5 /*anywheresoftware.b4a.objects.collections.List*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._vv1 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM especies",(String[])(anywheresoftware.b4a.keywords.Common.Null),(int) (0));
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public static String  _cargarbienvenidos_nuevo() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 440;BA.debugLine="Sub CargarBienvenidos_Nuevo";
 //BA.debugLineNum = 442;BA.debugLine="If modooffline = False Then";
if (_vvvvvvvvvvvvv1==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 445;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 446;BA.debugLine="dd.url = serverPath & \"/connect2/checkmessages.p";
_dd.url /*String*/  = _vvvvvvvvvvvvvv2+"/connect2/checkmessages.php?deviceID="+_vvvvvvvvvvvvvv1;
 //BA.debugLineNum = 447;BA.debugLine="dd.EventName = \"checkMessages\"";
_dd.EventName /*String*/  = "checkMessages";
 //BA.debugLineNum = 448;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = main.getObject();
 //BA.debugLineNum = 449;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\"";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 }else {
 //BA.debugLineNum = 451;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 452;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 453;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv3.getObject()));
 };
 //BA.debugLineNum = 455;BA.debugLine="End Sub";
return "";
}
public static String  _checkmessages_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
String _numresults = "";
int _i = 0;
anywheresoftware.b4a.objects.collections.Map _newpunto = null;
String _msg = "";
anywheresoftware.b4a.objects.collections.Map _map1 = null;
anywheresoftware.b4a.objects.collections.Map _usrmap = null;
 //BA.debugLineNum = 456;BA.debugLine="Sub checkMessages_Complete(Job As HttpJob)";
 //BA.debugLineNum = 457;BA.debugLine="Log(\"Mensajes chequeados: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("5786433","Mensajes chequeados: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 458;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 460;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 461;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 462;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 463;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 464;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 465;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 466;BA.debugLine="modooffline = False";
_vvvvvvvvvvvvv1 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 467;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 468;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 }else if((_act).equals("Error")) { 
 }else if((_act).equals("MensajesOK")) { 
 //BA.debugLineNum = 472;BA.debugLine="Dim numresults As String";
_numresults = "";
 //BA.debugLineNum = 473;BA.debugLine="numresults = parser.NextValue";
_numresults = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 474;BA.debugLine="For i = 0 To numresults - 1";
{
final int step16 = 1;
final int limit16 = (int) ((double)(Double.parseDouble(_numresults))-1);
_i = (int) (0) ;
for (;_i <= limit16 ;_i = _i + step16 ) {
 //BA.debugLineNum = 475;BA.debugLine="Dim newpunto As Map";
_newpunto = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 476;BA.debugLine="newpunto = parser.NextObject";
_newpunto = _parser.NextObject();
 //BA.debugLineNum = 477;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 484;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 485;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 486;BA.debugLine="Map1.Put(\"serverid\", newpunto.Get(\"serverid\"))";
_map1.Put((Object)("serverid"),_newpunto.Get((Object)("serverid")));
 //BA.debugLineNum = 487;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","valorOrganismo",_newpunto.Get((Object)("mensaje_type")),_map1);
 //BA.debugLineNum = 488;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","estadoValidacion",(Object)("validado"),_map1);
 //BA.debugLineNum = 490;BA.debugLine="If newpunto.Get(\"mensaje_type\") = \"invalido\" Th";
if ((_newpunto.Get((Object)("mensaje_type"))).equals((Object)("invalido"))) { 
 //BA.debugLineNum = 492;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 493;BA.debugLine="msg = utilidades.Mensaje(\"Identificación\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identificación","MsgMensaje.png","Notificación privada",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok gracias!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 495;BA.debugLine="msg = utilidades.Mensaje(\"Identification\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identification","MsgMensaje.png","Private notification",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok thank you!"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else if((_newpunto.Get((Object)("mensaje_type"))).equals((Object)("ninfa"))) { 
 //BA.debugLineNum = 501;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 502;BA.debugLine="msg = utilidades.Mensaje(\"Identificación\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identificación","MsgMensaje.png","Notificación privada",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok gracias!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 504;BA.debugLine="msg = utilidades.Mensaje(\"Identification\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identification","MsgMensaje.png","Private notification",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok thank you!"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else if((_newpunto.Get((Object)("mensaje_type"))).equals((Object)("novinchuca"))) { 
 //BA.debugLineNum = 509;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 510;BA.debugLine="msg = utilidades.Mensaje(\"Identificación\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identificación","MsgMensaje.png","Notificación privada",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok gracias!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 512;BA.debugLine="msg = utilidades.Mensaje(\"Identification\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identification","MsgMensaje.png","Private notification",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)(""),(Object)(""),(Object)("Ok thank you!"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else if((_newpunto.Get((Object)("mensaje_type"))).equals((Object)("sospechoso"))) { 
 //BA.debugLineNum = 517;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 518;BA.debugLine="msg = utilidades.Mensaje(\"Identificación\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identificación","MsgVinchuca.png","Notificación privada",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)("Ver detalles"),(Object)(""),(Object)("Ok, gracias!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 520;BA.debugLine="msg = utilidades.Mensaje(\"Identification\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identification","MsgVinchuca.png","Private notification",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)("View details"),(Object)(""),(Object)("Ok, thank you!"),anywheresoftware.b4a.keywords.Common.True);
 };
 }else {
 //BA.debugLineNum = 524;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 525;BA.debugLine="msg = utilidades.Mensaje(\"Identificación\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identificación","MsgVinchuca.png","Notificación privada",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)("Ver detalles"),(Object)(""),(Object)("Ok, gracias!"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 527;BA.debugLine="msg = utilidades.Mensaje(\"Identification\", \"";
_msg = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Identification","MsgVinchuca.png","Private notification",BA.ObjectToString(_newpunto.Get((Object)("mensaje"))),(Object)("View details"),(Object)(""),(Object)("Ok, thank you!"),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 531;BA.debugLine="If msg = DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 533;BA.debugLine="frmDatosAnteriores.notificacion = True";
mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv3 /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 534;BA.debugLine="frmDatosAnteriores.serverIdNum = newpunto.Get";
mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv4 /*String*/  = BA.ObjectToString(_newpunto.Get((Object)("serverid")));
 };
 };
 }
};
 };
 //BA.debugLineNum = 541;BA.debugLine="Dim usrMap As Map";
_usrmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 542;BA.debugLine="usrMap.Initialize";
_usrmap.Initialize();
 //BA.debugLineNum = 543;BA.debugLine="usrMap = DBUtils.ExecuteMap(Starter.sqlDB, \"SELE";
_usrmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM userconfig WHERE lastUser = 'si'",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 544;BA.debugLine="If usrMap = Null Or usrMap.IsInitialized = False";
if (_usrmap== null || _usrmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 546;BA.debugLine="username = \"guest\"";
_vvvvvvvvvvvv7 = "guest";
 //BA.debugLineNum = 547;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 548;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 550;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv3.getObject()));
 }else {
 //BA.debugLineNum = 553;BA.debugLine="username = usrMap.Get(\"username\")";
_vvvvvvvvvvvv7 = BA.ObjectToString(_usrmap.Get((Object)("username")));
 //BA.debugLineNum = 554;BA.debugLine="pass = usrMap.Get(\"pass\")";
_vvvvvvvvvvvv0 = BA.ObjectToString(_usrmap.Get((Object)("pass")));
 //BA.debugLineNum = 555;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 556;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 557;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv3.getObject()));
 };
 }else {
 //BA.debugLineNum = 563;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 564;BA.debugLine="MsgboxAsync(\"No tienes conexión a Internet, no";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No tienes conexión a Internet, no puedes recibir los mensajes de los revisores"),BA.ObjectToCharSequence("Advertencia"),processBA);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 566;BA.debugLine="MsgboxAsync(\"You are not connected to the inter";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("You are not connected to the internet, you cannot receive any messages from the experts"),BA.ObjectToCharSequence("Warning"),processBA);
 };
 //BA.debugLineNum = 569;BA.debugLine="modooffline = True";
_vvvvvvvvvvvvv1 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 570;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 571;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 572;BA.debugLine="StartActivity(frmprincipal)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv3.getObject()));
 };
 //BA.debugLineNum = 575;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 576;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 89;BA.debugLine="Dim versionactual As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = "";
 //BA.debugLineNum = 90;BA.debugLine="Dim serverversion As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 91;BA.debugLine="Dim servernewstitulo As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 92;BA.debugLine="Dim servernewstext As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 93;BA.debugLine="Dim msjprivadouser As String";
_vvvvvvvvvvvvv4 = "";
 //BA.debugLineNum = 95;BA.debugLine="Private pgbLoad As ProgressBar";
mostCurrent._pgbload = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 96;BA.debugLine="Private lblEstado As Label";
mostCurrent._lblestado = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 99;BA.debugLine="Private fondoblanco As Label";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 100;BA.debugLine="Private btnOffline As Button";
mostCurrent._btnoffline = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 104;BA.debugLine="Dim Cursor1 As Cursor";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
frmprincipal._process_globals();
starter._process_globals();
frmcomofotos._process_globals();
frmlogin._process_globals();
dbutils._process_globals();
downloadservice._process_globals();
firebasemessaging._process_globals();
frmabout._process_globals();
frmaprender._process_globals();
frmcamara._process_globals();
frmcomotransmiten._process_globals();
frmcomovemos_app._process_globals();
frmcomovemos_enfermedades._process_globals();
frmcomovemos_porqueexiste._process_globals();
frmdatosanteriores._process_globals();
frmdondecria._process_globals();
frmeditprofile._process_globals();
frmelmosquito._process_globals();
frmenfermedades._process_globals();
frmfotos._process_globals();
frmfotoscriadero._process_globals();
frmidentificarmosquito._process_globals();
frminfografias_main._process_globals();
frminstrucciones._process_globals();
frmlocalizacion._process_globals();
frmmapa._process_globals();
frmpoliticadatos._process_globals();
frmquehacer._process_globals();
httputils2service._process_globals();
multipartpost._process_globals();
register._process_globals();
uploadfiles._process_globals();
utilidades._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}

private static byte[][] bb;

public static String vvv13(final byte[] _b, final int i) throws Exception {
Runnable r = new Runnable() {
{

int value = i / 7 + 722671;
if (bb == null) {
		
                bb = new byte[4][];
				bb[0] = BA.packageName.getBytes("UTF8");
                bb[1] = BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionName.getBytes("UTF8");
                if (bb[1].length == 0)
                    bb[1] = "jsdkfh".getBytes("UTF8");
                bb[2] = new byte[] { (byte)BA.applicationContext.getPackageManager().getPackageInfo(BA.packageName, 0).versionCode };			
        }
        bb[3] = new byte[] {
                    (byte) (value >>> 24),
						(byte) (value >>> 16),
						(byte) (value >>> 8),
						(byte) value};
				try {
					for (int __b = 0;__b < (3 + 1);__b ++) {
						for (int b = 0;b<_b.length;b++) {
							_b[b] ^= bb[__b][b % bb[__b].length];
						}
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
                

            
}
public void run() {
}
};
return new String(_b, "UTF8");
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 23;BA.debugLine="Dim valorVinchuca As String";
_vvvvvvvvvv1 = "";
 //BA.debugLineNum = 24;BA.debugLine="Dim dateandtime As String";
_vvvvvvvvvv2 = "";
 //BA.debugLineNum = 25;BA.debugLine="Dim Idproyecto As Int";
_vvvvvvvvvv3 = 0;
 //BA.debugLineNum = 26;BA.debugLine="Dim longitud As String";
_vvvvvvvvvv4 = "";
 //BA.debugLineNum = 27;BA.debugLine="Dim latitud As String";
_vvvvvvvvvv5 = "";
 //BA.debugLineNum = 30;BA.debugLine="Dim valorMosquito As String";
_vvvvvvvvvv6 = "";
 //BA.debugLineNum = 31;BA.debugLine="Dim valorCriadero As String";
_vvvvvvvvvv7 = "";
 //BA.debugLineNum = 32;BA.debugLine="Dim valorCriadero1 As String";
_vvvvvvvvvv0 = "";
 //BA.debugLineNum = 33;BA.debugLine="Dim valorCriadero2 As String";
_vvvvvvvvvvv1 = "";
 //BA.debugLineNum = 34;BA.debugLine="Dim tipoevaluacion As String";
_vvvvvvvvvvv2 = "";
 //BA.debugLineNum = 37;BA.debugLine="Dim fotopath0 As String = \"\"";
_vvvvvvvvvvv3 = "";
 //BA.debugLineNum = 38;BA.debugLine="Dim fotopath1 As String = \"\"";
_vvvvvvvvvvv4 = "";
 //BA.debugLineNum = 39;BA.debugLine="Dim fotopath2 As String = \"\"";
_vvvvvvvvvvv5 = "";
 //BA.debugLineNum = 40;BA.debugLine="Dim fotopath3 As String = \"\"";
_vvvvvvvvvvv6 = "";
 //BA.debugLineNum = 44;BA.debugLine="Dim strUserID As String";
_vvvvvvvvvvv7 = "";
 //BA.debugLineNum = 45;BA.debugLine="Dim strUserName As String";
_vvvvvvvvvvv0 = "";
 //BA.debugLineNum = 46;BA.debugLine="Dim strUserLocation As String";
_vvvvvvvvvvvv1 = "";
 //BA.debugLineNum = 47;BA.debugLine="Dim strUserEmail As String";
_vvvvvvvvvvvv2 = "";
 //BA.debugLineNum = 48;BA.debugLine="Dim strUserFullName As String";
_vvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 49;BA.debugLine="Dim strUserOrg As String";
_vvvvvvvvvvvv4 = "";
 //BA.debugLineNum = 50;BA.debugLine="Dim strUserTipoUsuario As String";
_vvvvvvvvvvvv5 = "";
 //BA.debugLineNum = 51;BA.debugLine="Dim strUserGroup As String";
_vvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 52;BA.debugLine="Dim username As String";
_vvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 53;BA.debugLine="Dim pass As String";
_vvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 56;BA.debugLine="Dim modooffline As Boolean";
_vvvvvvvvvvvvv1 = false;
 //BA.debugLineNum = 57;BA.debugLine="Dim forceoffline As Boolean";
_vvvvvvvvvvvvv2 = false;
 //BA.debugLineNum = 59;BA.debugLine="Dim k As Phone";
_vv0 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 60;BA.debugLine="Dim savedir As String";
_v7 = "";
 //BA.debugLineNum = 63;BA.debugLine="Dim proyectoenviar As String";
_vvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 64;BA.debugLine="Dim msjprivadouser As String";
_vvvvvvvvvvvvv4 = "";
 //BA.debugLineNum = 65;BA.debugLine="Dim msjprivadoleido As Boolean";
_vvvvvvvvvvvvv5 = false;
 //BA.debugLineNum = 67;BA.debugLine="Dim firstuse As String";
_vvvvvvvvvvvvv6 = "";
 //BA.debugLineNum = 68;BA.debugLine="Dim valorVinchuca As String";
_vvvvvvvvvv1 = "";
 //BA.debugLineNum = 70;BA.debugLine="Dim currentproject As String";
_vvvvvvvvvvvvv7 = "";
 //BA.debugLineNum = 71;BA.debugLine="Dim datecurrentproject As String";
_vvvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 74;BA.debugLine="Dim deviceID As String";
_vvvvvvvvvvvvvv1 = "";
 //BA.debugLineNum = 75;BA.debugLine="Dim serverPath As String = \"https://www.cazamosqu";
_vvvvvvvvvvvvvv2 = main.vvv13 (new byte[] {43,37,73,-81,97,123,7,-2,38,46,69,-28,44,50,92,-91,48,109,89,-68,38,52,90,-72,39,113,71,-80,43,99,8,-67}, 632013);
 //BA.debugLineNum = 76;BA.debugLine="Dim subFolder As String = \"\"";
_vvvvvvvvvvvvvv3 = "";
 //BA.debugLineNum = 79;BA.debugLine="Dim speciesMap As List";
_vvvvvvvvvvvvvv4 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 82;BA.debugLine="Dim rp As RuntimePermissions";
_v6 = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 83;BA.debugLine="Dim lang As String";
_vvvvvvvvvvvvvv5 = "";
 //BA.debugLineNum = 84;BA.debugLine="Private timer1 As Timer";
_vvvvvvv1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 351;BA.debugLine="Sub TestInternet";
 //BA.debugLineNum = 352;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 353;BA.debugLine="dd.url = serverPath & \"/connect2/connecttest.php\"";
_dd.url /*String*/  = _vvvvvvvvvvvvvv2+"/connect2/connecttest.php";
 //BA.debugLineNum = 355;BA.debugLine="dd.EventName = \"TestInternet\"";
_dd.EventName /*String*/  = "TestInternet";
 //BA.debugLineNum = 356;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = main.getObject();
 //BA.debugLineNum = 357;BA.debugLine="btnOffline.Visible = True";
mostCurrent._btnoffline.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 358;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 359;BA.debugLine="End Sub";
return "";
}
public static String  _testinternet_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
anywheresoftware.b4a.objects.collections.Map _nd = null;
String _upd = "";
anywheresoftware.b4a.objects.IntentWrapper _market = null;
String _uri = "";
 //BA.debugLineNum = 360;BA.debugLine="Sub TestInternet_Complete(Job As HttpJob)";
 //BA.debugLineNum = 361;BA.debugLine="Log(\"Job completed: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("5655361","Job completed: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 362;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 363;BA.debugLine="versionactual = Application.VersionCode";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Application.getVersionCode());
 //BA.debugLineNum = 364;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 365;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 366;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 367;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 368;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 369;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 370;BA.debugLine="If act = \"Connected\" Then";
if ((_act).equals("Connected")) { 
 //BA.debugLineNum = 372;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 373;BA.debugLine="lblEstado.Text = \"Conectado. Comprobando versi";
mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Conectado. Comprobando versión de la aplicación"));
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 375;BA.debugLine="lblEstado.Text = \"Connected. Checking app vers";
mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Connected. Checking app version"));
 };
 //BA.debugLineNum = 378;BA.debugLine="modooffline = False";
_vvvvvvvvvvvvv1 = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 379;BA.debugLine="Dim nd As Map";
_nd = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 380;BA.debugLine="nd = parser.NextObject";
_nd = _parser.NextObject();
 //BA.debugLineNum = 381;BA.debugLine="serverversion = nd.Get(\"currentversion\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 = BA.ObjectToString(_nd.Get((Object)("currentversion")));
 //BA.debugLineNum = 382;BA.debugLine="servernewstitulo = nd.Get(\"newstitulo\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7 = BA.ObjectToString(_nd.Get((Object)("newstitulo")));
 //BA.debugLineNum = 383;BA.debugLine="servernewstext = nd.Get(\"newstext\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = BA.ObjectToString(_nd.Get((Object)("newstext")));
 //BA.debugLineNum = 384;BA.debugLine="If serverversion <> versionactual Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6).equals(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5) == false) { 
 //BA.debugLineNum = 385;BA.debugLine="Dim upd As String";
_upd = "";
 //BA.debugLineNum = 386;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 387;BA.debugLine="upd = Msgbox2(\"Para continuar, debe descargar";
_upd = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Para continuar, debe descargar una actualización importante"),BA.ObjectToCharSequence("Actualización"),"Ir a GooglePlay","Lo haré después","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 389;BA.debugLine="upd = Msgbox2(\"To continue, an important upda";
_upd = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("To continue, an important update has to be downloaded"),BA.ObjectToCharSequence("Update"),"Go to GooglePlay","I'll do it later","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 392;BA.debugLine="If upd = DialogResponse.POSITIVE Then";
if ((_upd).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 394;BA.debugLine="Dim market As Intent, uri As String";
_market = new anywheresoftware.b4a.objects.IntentWrapper();
_uri = "";
 //BA.debugLineNum = 395;BA.debugLine="uri=\"market://details?id=caza.mosquito\"";
_uri = "market://details?id=caza.mosquito";
 //BA.debugLineNum = 396;BA.debugLine="market.Initialize(market.ACTION_VIEW,uri)";
_market.Initialize(_market.ACTION_VIEW,_uri);
 //BA.debugLineNum = 397;BA.debugLine="StartActivity(market)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(_market.getObject()));
 //BA.debugLineNum = 399;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 400;BA.debugLine="CargarBienvenidos_Nuevo";
_cargarbienvenidos_nuevo();
 }else {
 //BA.debugLineNum = 403;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 404;BA.debugLine="CargarBienvenidos_Nuevo";
_cargarbienvenidos_nuevo();
 };
 }else {
 //BA.debugLineNum = 408;BA.debugLine="If servernewstitulo <> \"\" And servernewstitulo";
if ((mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7).equals("") == false && (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7).equals("Nada") == false) { 
 //BA.debugLineNum = 409;BA.debugLine="MsgboxAsync(servernewstext, servernewstitulo)";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0),BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7),processBA);
 };
 //BA.debugLineNum = 413;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 414;BA.debugLine="CargarBienvenidos_Nuevo";
_cargarbienvenidos_nuevo();
 };
 };
 }else {
 //BA.debugLineNum = 419;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 420;BA.debugLine="MsgboxAsync(\"No tienes conexión a Internet. Caz";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("No tienes conexión a Internet. Caza Mosquitos iniciará con el último usuario que utilizaste, pero no podrás ver ni cargar puntos hasta que no te conectes!"),BA.ObjectToCharSequence("Advertencia"),processBA);
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 422;BA.debugLine="MsgboxAsync(\"You have no connection to the inte";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("You have no connection to the internet. Caza Mosquitos will start with the last user that logged in, but you won't be able to send data unless you have internet!"),BA.ObjectToCharSequence("Warning"),processBA);
 };
 //BA.debugLineNum = 425;BA.debugLine="modooffline = True";
_vvvvvvvvvvvvv1 = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 427;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 429;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 430;BA.debugLine="CargarBienvenidos_Nuevo";
_cargarbienvenidos_nuevo();
 };
 //BA.debugLineNum = 432;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 433;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
 //BA.debugLineNum = 231;BA.debugLine="Sub timer1_Tick";
 //BA.debugLineNum = 232;BA.debugLine="If lang = \"es\" Then";
if ((_vvvvvvvvvvvvvv5).equals("es")) { 
 //BA.debugLineNum = 233;BA.debugLine="lblEstado.Text = \"Comprobando conexión a interne";
mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Comprobando conexión a internet"));
 }else if((_vvvvvvvvvvvvvv5).equals("en")) { 
 //BA.debugLineNum = 235;BA.debugLine="lblEstado.Text = \"Checking the connection to the";
mostCurrent._lblestado.setText(BA.ObjectToCharSequence("Checking the connection to the internet"));
 };
 //BA.debugLineNum = 240;BA.debugLine="TestInternet";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3();
 //BA.debugLineNum = 242;BA.debugLine="timer1.Enabled = False";
_vvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4(String _columnname) throws Exception{
String _mytable = "";
String _txt = "";
int _cols = 0;
String[] _mycolname = null;
String _newcolumn = "";
boolean _flag = false;
int _i = 0;
 //BA.debugLineNum = 314;BA.debugLine="Sub UpdateInternalDB(columnname As String)";
 //BA.debugLineNum = 317;BA.debugLine="Dim MyTable As String = \"userconfig\"";
_mytable = "userconfig";
 //BA.debugLineNum = 318;BA.debugLine="Dim txt As String";
_txt = "";
 //BA.debugLineNum = 319;BA.debugLine="txt=\"SELECT * FROM \" & MyTable & \" LIMIT 1\"";
_txt = "SELECT * FROM "+_mytable+" LIMIT 1";
 //BA.debugLineNum = 320;BA.debugLine="Cursor1=Starter.sqlDB.ExecQuery(txt)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ .ExecQuery(_txt)));
 //BA.debugLineNum = 321;BA.debugLine="Cursor1.Position=0";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.setPosition((int) (0));
 //BA.debugLineNum = 322;BA.debugLine="Dim cols As Int = Cursor1.ColumnCount";
_cols = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getColumnCount();
 //BA.debugLineNum = 323;BA.debugLine="Dim MyColName(cols) As String";
_mycolname = new String[_cols];
java.util.Arrays.fill(_mycolname,"");
 //BA.debugLineNum = 324;BA.debugLine="Dim NewColumn As String = columnname";
_newcolumn = _columnname;
 //BA.debugLineNum = 325;BA.debugLine="Dim Flag As Boolean =False";
_flag = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 327;BA.debugLine="For i=0 To Cursor1.ColumnCount-1";
{
final int step10 = 1;
final int limit10 = (int) (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.getColumnCount()-1);
_i = (int) (0) ;
for (;_i <= limit10 ;_i = _i + step10 ) {
 //BA.debugLineNum = 328;BA.debugLine="MyColName(i)=Cursor1.GetColumnName(i)";
_mycolname[_i] = mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2.GetColumnName(_i);
 //BA.debugLineNum = 329;BA.debugLine="If MyColName(i)= NewColumn Then";
if ((_mycolname[_i]).equals(_newcolumn)) { 
 //BA.debugLineNum = 331;BA.debugLine="Log(\"Columna \" &columnname &\" existe\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5524305","Columna "+_columnname+" existe",0);
 //BA.debugLineNum = 332;BA.debugLine="Flag=True";
_flag = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 333;BA.debugLine="Exit";
if (true) break;
 };
 }
};
 //BA.debugLineNum = 336;BA.debugLine="If Flag=False Then";
if (_flag==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 337;BA.debugLine="txt=\"ALTER TABLE \" & MyTable & \" ADD COLUMN \" &";
_txt = "ALTER TABLE "+_mytable+" ADD COLUMN "+_newcolumn+" VARCHAR(100)";
 //BA.debugLineNum = 338;BA.debugLine="Starter.sqlDB.ExecNonQuery(txt)";
mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ .ExecNonQuery(_txt);
 //BA.debugLineNum = 339;BA.debugLine="ToastMessageShow(\"Base de datos interna actualiz";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Base de datos interna actualizada / Internal DB checked"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 340;BA.debugLine="Log(\"Columna \" &columnname & \" agregada\")";
anywheresoftware.b4a.keywords.Common.LogImpl("5524314","Columna "+_columnname+" agregada",0);
 };
 //BA.debugLineNum = 344;BA.debugLine="End Sub";
return "";
}
}
