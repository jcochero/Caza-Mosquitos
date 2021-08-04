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

public class frmprincipal extends Activity implements B4AActivity{
	public static frmprincipal mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmprincipal");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmprincipal).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmprincipal");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmprincipal", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmprincipal) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmprincipal) Resume **");
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
		return frmprincipal.class;
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
            BA.LogInfo("** Activity (frmprincipal) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmprincipal) Pause event (activity is not paused). **");
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
            frmprincipal mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmprincipal) Resume **");
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
public static String _v5 = "";
public static anywheresoftware.b4a.objects.RuntimePermissions _v6 = null;
public static String _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
public static boolean _resetting_msg = false;
public anywheresoftware.b4a.objects.TabStripViewPager _tabstripmain = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnmenu = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblencontremosquito = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btninformemosquito = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblencontrecriadero = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btninformecriadero = null;
public anywheresoftware.b4a.phone.Phone _vvvvvvv6 = null;
public caza.mosquito.b4xdrawer _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _btncomofotosreportar = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerrarsesion = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnedituser = null;
public anywheresoftware.b4a.objects.LabelWrapper _btndatosanteriores = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblusername = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblregistrate = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnverperfil = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnabout = null;
public anywheresoftware.b4a.objects.LabelWrapper _btnpoliticadatos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgrupo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblusernamemain = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblgrupomain = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
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

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 54;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 56;BA.debugLine="p.SetScreenOrientation(1)";
mostCurrent._vvvvvvv6.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 58;BA.debugLine="Drawer.Initialize(Me, \"Drawer\", Activity, 300dip)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._initialize /*String*/ (mostCurrent.activityBA,frmprincipal.getObject(),"Drawer",(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (300)));
 //BA.debugLineNum = 59;BA.debugLine="Drawer.CenterPanel.LoadLayout(\"frmPrincipal_CM2\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._getvvvvvvvvvvvvvvv7 /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("frmPrincipal_CM2",mostCurrent.activityBA);
 //BA.debugLineNum = 60;BA.debugLine="Drawer.LeftPanel.LoadLayout(\"frmSideNav\")";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._getvvvvvvvvvvvvvvvv2 /*anywheresoftware.b4a.objects.B4XViewWrapper*/ ().LoadLayout("frmSideNav",mostCurrent.activityBA);
 //BA.debugLineNum = 92;BA.debugLine="If frmDatosAnteriores.notificacion = True Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv3 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 93;BA.debugLine="CallSubDelayed3(frmDatosAnteriores,\"VerDetalles\"";
anywheresoftware.b4a.keywords.Common.CallSubDelayed3(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv6.getObject()),"VerDetalles",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv4 /*String*/ ),(Object)(anywheresoftware.b4a.keywords.Common.True));
 };
 //BA.debugLineNum = 97;BA.debugLine="If Main.modooffline = True Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 98;BA.debugLine="startBienvienido(False, True)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6(anywheresoftware.b4a.keywords.Common.False,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 99;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 101;BA.debugLine="startBienvienido(True, True)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6(anywheresoftware.b4a.keywords.Common.True,anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 104;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 105;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
String _msg = "";
 //BA.debugLineNum = 140;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 142;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 143;BA.debugLine="If Drawer.LeftOpen Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._getvvvvvvvvvvvvvvvv1 /*boolean*/ ()) { 
 //BA.debugLineNum = 144;BA.debugLine="Drawer.LeftOpen = False";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._setvvvvvvvvvvvvvvvv1 /*boolean*/ (anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 147;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 148;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 149;BA.debugLine="msg = Msgbox2(\"Salir de la aplicación?\", \"SAL";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Salir de la aplicación?"),BA.ObjectToCharSequence("SALIR"),"Si, deseo salir","","No, me equivoqué",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 151;BA.debugLine="msg = Msgbox2(\"Exit the app?\", \"EXIT\", \"Yes, e";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Exit the app?"),BA.ObjectToCharSequence("EXIT"),"Yes, exit","","Nop, my bad",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 153;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 154;BA.debugLine="ExitApplication";
anywheresoftware.b4a.keywords.Common.ExitApplication();
 };
 };
 //BA.debugLineNum = 157;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 159;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 137;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 139;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 107;BA.debugLine="If Main.strUserGroup <> \"\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv6 /*String*/ ).equals("") == false) { 
 //BA.debugLineNum = 108;BA.debugLine="lblGrupo.Text = Main.strUserGroup";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv6 /*String*/ ));
 //BA.debugLineNum = 109;BA.debugLine="lblGrupoMain.text = Main.strUserGroup";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv6 /*String*/ ));
 }else {
 //BA.debugLineNum = 111;BA.debugLine="lblGrupo.Text = \"¡Aún no trabajas en grupo!\"";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 //BA.debugLineNum = 112;BA.debugLine="lblGrupoMain.text = \"¡Aún no trabajas en grupo!\"";
mostCurrent._lblgrupomain.setText(BA.ObjectToCharSequence("¡Aún no trabajas en grupo!"));
 };
 //BA.debugLineNum = 115;BA.debugLine="If Main.username = \"guest\" Or Main.username = \"\"";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest") || (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("")) { 
 //BA.debugLineNum = 116;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 117;BA.debugLine="btnCerrarSesion.Text = \"Iniciar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Iniciar sesión"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 119;BA.debugLine="btnCerrarSesion.Text = \"Start session\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Start session"));
 };
 //BA.debugLineNum = 122;BA.debugLine="lblUserName.Visible = False";
mostCurrent._lblusername.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 123;BA.debugLine="lblRegistrate.Visible = True";
mostCurrent._lblregistrate.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 124;BA.debugLine="btnEditUser.Visible = False";
mostCurrent._btnedituser.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 126;BA.debugLine="lblUserName.Text = Main.username";
mostCurrent._lblusername.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 127;BA.debugLine="lblUserNameMain.Text = \"¡Hola, \" & Main.username";
mostCurrent._lblusernamemain.setText(BA.ObjectToCharSequence("¡Hola, "+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ +"!"));
 //BA.debugLineNum = 128;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 129;BA.debugLine="btnCerrarSesion.Text = \"Cerrar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Cerrar sesión"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 131;BA.debugLine="btnCerrarSesion.Text = \"Close session\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Close session"));
 };
 //BA.debugLineNum = 133;BA.debugLine="lblRegistrate.Visible = False";
mostCurrent._lblregistrate.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="btnEditUser.Visible = True";
mostCurrent._btnedituser.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _btnabout_click() throws Exception{
 //BA.debugLineNum = 229;BA.debugLine="Sub btnAbout_Click";
 //BA.debugLineNum = 230;BA.debugLine="StartActivity(frmabout)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv2.getObject()));
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _btnaccion_click() throws Exception{
 //BA.debugLineNum = 595;BA.debugLine="Sub btnAccion_Click";
 //BA.debugLineNum = 596;BA.debugLine="StartActivity(frmQueHacer)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvv2.getObject()));
 //BA.debugLineNum = 597;BA.debugLine="End Sub";
return "";
}
public static String  _btnaprender_click() throws Exception{
 //BA.debugLineNum = 573;BA.debugLine="Sub btnAprender_Click";
 //BA.debugLineNum = 574;BA.debugLine="StartActivity(frmInfografias_Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frminfografias_main.getObject()));
 //BA.debugLineNum = 575;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarsesion_click() throws Exception{
String _msg = "";
 //BA.debugLineNum = 208;BA.debugLine="Sub btnCerrarSesion_Click";
 //BA.debugLineNum = 209;BA.debugLine="Dim msg As String";
_msg = "";
 //BA.debugLineNum = 210;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 211;BA.debugLine="msg = Msgbox2(\"Desea cerrar la sesión? Ingresar";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Desea cerrar la sesión? Ingresar con otro usuario requiere de internet!"),BA.ObjectToCharSequence("Seguro?"),"Si, tengo internet","","No, no tengo internet ahora",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 213;BA.debugLine="msg = Msgbox2(\"Do you want to close the session?";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Do you want to close the session? To log in with another user you need an internet connection!"),BA.ObjectToCharSequence("Sure?"),"Yes, I have internet","","No, i'm offline now",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 215;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 216;BA.debugLine="Main.strUserID = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv7 /*String*/  = "";
 //BA.debugLineNum = 217;BA.debugLine="Main.strUserName = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv0 /*String*/  = "";
 //BA.debugLineNum = 218;BA.debugLine="Main.strUserLocation = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv1 /*String*/  = "";
 //BA.debugLineNum = 219;BA.debugLine="Main.strUserEmail = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv2 /*String*/  = "";
 //BA.debugLineNum = 220;BA.debugLine="Main.strUserOrg = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv4 /*String*/  = "";
 //BA.debugLineNum = 221;BA.debugLine="Main.username = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/  = "";
 //BA.debugLineNum = 222;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 223;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 225;BA.debugLine="CallSubDelayed(frmLogin, \"SignOutGoogle\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()),"SignOutGoogle");
 //BA.debugLineNum = 226;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 };
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _btncomofotosreportar_click() throws Exception{
 //BA.debugLineNum = 543;BA.debugLine="Sub btnComoFotosReportar_Click";
 //BA.debugLineNum = 544;BA.debugLine="StartActivity(frmComoFotos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv5.getObject()));
 //BA.debugLineNum = 546;BA.debugLine="End Sub";
return "";
}
public static String  _btndatosanteriores_click() throws Exception{
 //BA.debugLineNum = 547;BA.debugLine="Sub btnDatosAnteriores_Click";
 //BA.debugLineNum = 549;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 550;BA.debugLine="StartActivity(frmDatosAnteriores)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv6.getObject()));
 }else {
 //BA.debugLineNum = 552;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 554;BA.debugLine="ToastMessageShow(\"No ha iniciado sesión aún\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No ha iniciado sesión aún"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 557;BA.debugLine="ToastMessageShow(\"You haven't logged in yet\", F";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You haven't logged in yet"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
return "";
}
public static String  _btnedituser_click() throws Exception{
 //BA.debugLineNum = 232;BA.debugLine="Sub btnEditUser_Click";
 //BA.debugLineNum = 233;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 234;BA.debugLine="StartActivity(frmEditProfile)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv0.getObject()));
 }else {
 //BA.debugLineNum = 236;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 237;BA.debugLine="ToastMessageShow(\"Necesita tener internet para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita tener internet para editar su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 239;BA.debugLine="ToastMessageShow(\"You need to be online to chec";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be online to check your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static void  _btninformecriadero_click() throws Exception{
ResumableSub_btnInformeCriadero_Click rsub = new ResumableSub_btnInformeCriadero_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnInformeCriadero_Click extends BA.ResumableSub {
public ResumableSub_btnInformeCriadero_Click(caza.mosquito.frmprincipal parent) {
this.parent = parent;
}
caza.mosquito.frmprincipal parent;
int _iresult = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 417;BA.debugLine="If resetting_msg = True Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._resetting_msg==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 418;BA.debugLine="If Main.lang = \"es\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
this.state = 6;
}else if((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 419;BA.debugLine="ToastMessageShow(\"Buscando señal... aguarde uno";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Buscando señal... aguarde unos segundos y vuelva a intentarlo"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 421;BA.debugLine="ToastMessageShow(\"Looking for signal... please";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Looking for signal... please wait a few seconds and try again"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 10;
;
 //BA.debugLineNum = 423;BA.debugLine="Return";
if (true) return ;
 if (true) break;
;
 //BA.debugLineNum = 426;BA.debugLine="If Main.username = \"\" Or Main.username = \"guest\"";

case 10:
//if
this.state = 29;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("") || (parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest")) { 
this.state = 12;
}else {
this.state = 28;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 427;BA.debugLine="If Main.lang = \"es\" Then";
if (true) break;

case 13:
//if
this.state = 26;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
this.state = 15;
}else if((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 428;BA.debugLine="Msgbox2Async(\"Para enviar un reporte de un cria";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Para enviar un reporte de un criadero, primero tiene que tener un usuario. Es gratis!"),BA.ObjectToCharSequence("Usuario no registrado"),"Registrarme","Cancelar","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 429;BA.debugLine="Wait For MsgBox_Result (iResult As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 16;
_iresult = (Integer) result[0];
;
 //BA.debugLineNum = 430;BA.debugLine="If iResult = DialogResponse.POSITIVE Then";
if (true) break;

case 16:
//if
this.state = 19;
if (_iresult==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 431;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 if (true) break;

case 19:
//C
this.state = 26;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 434;BA.debugLine="Msgbox2Async(\"To report a breeding place, you n";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("To report a breeding place, you need to login first!"),BA.ObjectToCharSequence("Login"),"Sign up","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 435;BA.debugLine="Wait For MsgBox_Result (iResult As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 22;
_iresult = (Integer) result[0];
;
 //BA.debugLineNum = 436;BA.debugLine="If iResult = DialogResponse.POSITIVE Then";
if (true) break;

case 22:
//if
this.state = 25;
if (_iresult==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 437;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = 29;
;
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 441;BA.debugLine="ComenzarReporte(\"Criadero\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7("Criadero");
 if (true) break;

case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _msgbox_result(int _iresult) throws Exception{
}
public static void  _btninformemosquito_click() throws Exception{
ResumableSub_btnInformeMosquito_Click rsub = new ResumableSub_btnInformeMosquito_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnInformeMosquito_Click extends BA.ResumableSub {
public ResumableSub_btnInformeMosquito_Click(caza.mosquito.frmprincipal parent) {
this.parent = parent;
}
caza.mosquito.frmprincipal parent;
int _iresult = 0;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 381;BA.debugLine="If resetting_msg = True Then";
if (true) break;

case 1:
//if
this.state = 10;
if (parent._resetting_msg==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 382;BA.debugLine="If Main.lang = \"es\" Then";
if (true) break;

case 4:
//if
this.state = 9;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
this.state = 6;
}else if((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
this.state = 8;
}if (true) break;

case 6:
//C
this.state = 9;
 //BA.debugLineNum = 383;BA.debugLine="ToastMessageShow(\"Buscando señal... aguarde uno";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Buscando señal... aguarde unos segundos y vuelva a intentarlo"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 8:
//C
this.state = 9;
 //BA.debugLineNum = 385;BA.debugLine="ToastMessageShow(\"Looking for signal... please";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Looking for signal... please wait a few seconds and try again"),anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 10;
;
 //BA.debugLineNum = 388;BA.debugLine="Return";
if (true) return ;
 if (true) break;
;
 //BA.debugLineNum = 391;BA.debugLine="If Main.username = \"\" Or Main.username = \"guest\"";

case 10:
//if
this.state = 29;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("") || (parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest")) { 
this.state = 12;
}else {
this.state = 28;
}if (true) break;

case 12:
//C
this.state = 13;
 //BA.debugLineNum = 392;BA.debugLine="If Main.lang = \"es\" Then";
if (true) break;

case 13:
//if
this.state = 26;
if ((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
this.state = 15;
}else if((parent.mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
this.state = 21;
}if (true) break;

case 15:
//C
this.state = 16;
 //BA.debugLineNum = 393;BA.debugLine="Msgbox2Async(\"Para enviar un reporte de un mosq";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("Para enviar un reporte de un mosquito, primero tiene que tener un usuario. Es gratis!"),BA.ObjectToCharSequence("Usuario no registrado"),"Registrarme!","Cancelar","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 394;BA.debugLine="Wait For MsgBox_Result (iResult As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 30;
return;
case 30:
//C
this.state = 16;
_iresult = (Integer) result[0];
;
 //BA.debugLineNum = 395;BA.debugLine="If iResult = DialogResponse.POSITIVE Then";
if (true) break;

case 16:
//if
this.state = 19;
if (_iresult==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 18;
}if (true) break;

case 18:
//C
this.state = 19;
 //BA.debugLineNum = 396;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 if (true) break;

case 19:
//C
this.state = 26;
;
 if (true) break;

case 21:
//C
this.state = 22;
 //BA.debugLineNum = 399;BA.debugLine="Msgbox2Async(\"To report a mosquito, you need to";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("To report a mosquito, you need to login first. It's free!"),BA.ObjectToCharSequence("Login"),"Sign up","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 400;BA.debugLine="Wait For MsgBox_Result (iResult As Int)";
anywheresoftware.b4a.keywords.Common.WaitFor("msgbox_result", processBA, this, null);
this.state = 31;
return;
case 31:
//C
this.state = 22;
_iresult = (Integer) result[0];
;
 //BA.debugLineNum = 401;BA.debugLine="If iResult = DialogResponse.POSITIVE Then";
if (true) break;

case 22:
//if
this.state = 25;
if (_iresult==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
this.state = 24;
}if (true) break;

case 24:
//C
this.state = 25;
 //BA.debugLineNum = 402;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 if (true) break;

case 25:
//C
this.state = 26;
;
 if (true) break;

case 26:
//C
this.state = 29;
;
 if (true) break;

case 28:
//C
this.state = 29;
 //BA.debugLineNum = 406;BA.debugLine="ComenzarReporte(\"Mosquito\")";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7("Mosquito");
 if (true) break;

case 29:
//C
this.state = -1;
;
 //BA.debugLineNum = 410;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _btnlangen_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 262;BA.debugLine="Sub btnLangEn_Click";
 //BA.debugLineNum = 263;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 264;BA.debugLine="Main.lang = \"en\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/  = "en";
 //BA.debugLineNum = 265;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 266;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 267;BA.debugLine="Map1.Put(\"configID\", \"1\")";
_map1.Put((Object)("configID"),(Object)("1"));
 //BA.debugLineNum = 268;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"appconfig\",";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"appconfig","applang",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ),_map1);
 //BA.debugLineNum = 270;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 271;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 272;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,frmprincipal.getObject());
 };
 //BA.debugLineNum = 275;BA.debugLine="End Sub";
return "";
}
public static String  _btnlanges_click() throws Exception{
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 248;BA.debugLine="Sub btnLangEs_Click";
 //BA.debugLineNum = 249;BA.debugLine="If Main.lang = \"en\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 250;BA.debugLine="Main.lang = \"es\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/  = "es";
 //BA.debugLineNum = 251;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 252;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 253;BA.debugLine="Map1.Put(\"configID\", \"1\")";
_map1.Put((Object)("configID"),(Object)("1"));
 //BA.debugLineNum = 254;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"appconfig\",";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"appconfig","applang",(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ),_map1);
 //BA.debugLineNum = 256;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 257;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 258;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,frmprincipal.getObject());
 };
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _btnmenu_click() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub btnMenu_Click";
 //BA.debugLineNum = 176;BA.debugLine="Drawer.LeftOpen = True";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5._setvvvvvvvvvvvvvvvv1 /*boolean*/ (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 177;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 178;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 179;BA.debugLine="btnCerrarSesion.Text = \"Iniciar sesión\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Iniciar sesión"));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 181;BA.debugLine="btnCerrarSesion.Text = \"Sign in\"";
mostCurrent._btncerrarsesion.setText(BA.ObjectToCharSequence("Sign in"));
 };
 };
 //BA.debugLineNum = 184;BA.debugLine="lblGrupo.Text = Main.strUserGroup";
mostCurrent._lblgrupo.setText(BA.ObjectToCharSequence(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv6 /*String*/ ));
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _btnpoliticadatos_click() throws Exception{
 //BA.debugLineNum = 244;BA.debugLine="Sub btnPoliticaDatos_Click";
 //BA.debugLineNum = 245;BA.debugLine="StartActivity(frmPoliticaDatos)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvvv1.getObject()));
 //BA.debugLineNum = 246;BA.debugLine="End Sub";
return "";
}
public static String  _btnverperfil_click() throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub btnVerPerfil_Click";
 //BA.debugLineNum = 188;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 189;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 190;BA.debugLine="ToastMessageShow(\"Necesita estar registrado par";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita estar registrado para ver su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 192;BA.debugLine="ToastMessageShow(\"You need to be registered to";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be registered to view your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 }else {
 //BA.debugLineNum = 195;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 196;BA.debugLine="StartActivity(frmEditProfile)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv0.getObject()));
 }else {
 //BA.debugLineNum = 198;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 199;BA.debugLine="ToastMessageShow(\"Necesita tener internet para";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Necesita tener internet para editar su perfil"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 201;BA.debugLine="ToastMessageShow(\"You need to be online to che";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You need to be online to check your profile"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 };
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _butexplorarmapa_click() throws Exception{
 //BA.debugLineNum = 585;BA.debugLine="Sub butExplorarMapa_Click";
 //BA.debugLineNum = 586;BA.debugLine="StartActivity(frmMapa)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv0.getObject()));
 //BA.debugLineNum = 587;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv7(String _tiporeporte) throws Exception{
anywheresoftware.b4a.objects.collections.List _neweval = null;
anywheresoftware.b4a.objects.collections.Map _m = null;
anywheresoftware.b4a.objects.collections.Map _currentprojectmap = null;
anywheresoftware.b4a.objects.collections.Map _map1 = null;
 //BA.debugLineNum = 450;BA.debugLine="Sub ComenzarReporte(tiporeporte As String)";
 //BA.debugLineNum = 453;BA.debugLine="If Main.username = \"guest\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ).equals("guest")) { 
 //BA.debugLineNum = 454;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 455;BA.debugLine="ToastMessageShow(\"Para enviar un reporte debe e";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Para enviar un reporte debe estar registrado"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 457;BA.debugLine="ToastMessageShow(\"To send a report, you have to";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("To send a report, you have to be registered"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 459;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 460;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 461;BA.debugLine="StartActivity(frmLogin)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()));
 //BA.debugLineNum = 462;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 467;BA.debugLine="Main.fotopath0 = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv3 /*String*/  = "";
 //BA.debugLineNum = 468;BA.debugLine="Main.fotopath1 = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv4 /*String*/  = "";
 //BA.debugLineNum = 469;BA.debugLine="Main.fotopath2 = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv5 /*String*/  = "";
 //BA.debugLineNum = 470;BA.debugLine="Main.fotopath3 = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv6 /*String*/  = "";
 //BA.debugLineNum = 471;BA.debugLine="Main.latitud = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv5 /*String*/  = "";
 //BA.debugLineNum = 472;BA.debugLine="Main.longitud = \"\"";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv4 /*String*/  = "";
 //BA.debugLineNum = 473;BA.debugLine="Main.tipoevaluacion = tiporeporte";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv2 /*String*/  = _tiporeporte;
 //BA.debugLineNum = 477;BA.debugLine="Dim newEval As List";
_neweval = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 478;BA.debugLine="newEval.Initialize";
_neweval.Initialize();
 //BA.debugLineNum = 479;BA.debugLine="Dim m As Map";
_m = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 480;BA.debugLine="m.Initialize";
_m.Initialize();
 //BA.debugLineNum = 481;BA.debugLine="m.Put(\"usuario\", Main.username)";
_m.Put((Object)("usuario"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 482;BA.debugLine="m.Put(\"tipoEval\", tiporeporte)";
_m.Put((Object)("tipoEval"),(Object)(_tiporeporte));
 //BA.debugLineNum = 483;BA.debugLine="newEval.Add(m)";
_neweval.Add((Object)(_m.getObject()));
 //BA.debugLineNum = 484;BA.debugLine="DBUtils.InsertMaps(Starter.sqlDB,\"markers_local\",";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv1 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local",_neweval);
 //BA.debugLineNum = 492;BA.debugLine="Dim currentprojectMap As Map";
_currentprojectmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 493;BA.debugLine="currentprojectMap.Initialize";
_currentprojectmap.Initialize();
 //BA.debugLineNum = 494;BA.debugLine="Try";
try { //BA.debugLineNum = 495;BA.debugLine="currentprojectMap = DBUtils.ExecuteMap(Starter.s";
_currentprojectmap = mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvv4 /*anywheresoftware.b4a.objects.collections.Map*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"SELECT * FROM markers_local ORDER BY id DESC LIMIT 1",(String[])(anywheresoftware.b4a.keywords.Common.Null));
 //BA.debugLineNum = 496;BA.debugLine="If currentprojectMap = Null Or currentprojectMap";
if (_currentprojectmap== null || _currentprojectmap.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 498;BA.debugLine="Main.currentproject = 1";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/  = BA.NumberToString(1);
 //BA.debugLineNum = 499;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 500;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 501;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 502;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 503;BA.debugLine="Main.datecurrentproject = DateTime.Date(DateTim";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv0 /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 511;BA.debugLine="fullidcurrentproject = Main.username & \"_\" & Ma";
_v5 = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ +"_"+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ +"_"+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv0 /*String*/ ;
 //BA.debugLineNum = 512;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","fullID",(Object)(_v5),_map1);
 }else {
 //BA.debugLineNum = 514;BA.debugLine="Main.currentproject = currentprojectMap.Get(\"id";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/  = BA.ObjectToString(_currentprojectmap.Get((Object)("id")));
 //BA.debugLineNum = 515;BA.debugLine="Dim Map1 As Map";
_map1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 516;BA.debugLine="Map1.Initialize";
_map1.Initialize();
 //BA.debugLineNum = 517;BA.debugLine="Map1.Put(\"Id\", Main.currentproject)";
_map1.Put((Object)("Id"),(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 518;BA.debugLine="DateTime.DateFormat = \"dd-MM-yyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("dd-MM-yyyy");
 //BA.debugLineNum = 519;BA.debugLine="Main.datecurrentproject = DateTime.Date(DateTim";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv0 /*String*/  = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 527;BA.debugLine="fullidcurrentproject = Main.username & \"_\" & Ma";
_v5 = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ +"_"+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ +"_"+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv0 /*String*/ ;
 //BA.debugLineNum = 528;BA.debugLine="DBUtils.UpdateRecord(Starter.sqlDB, \"markers_lo";
mostCurrent._vvvvvvvvvvvvvvvvvv7._vvvvv5 /*String*/ (mostCurrent.activityBA,mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ ,"markers_local","fullID",(Object)(_v5),_map1);
 };
 } 
       catch (Exception e51) {
			processBA.setLastException(e51); //BA.debugLineNum = 533;BA.debugLine="Log(LastException)";
anywheresoftware.b4a.keywords.Common.LogImpl("52228307",BA.ObjectToString(anywheresoftware.b4a.keywords.Common.LastException(mostCurrent.activityBA)),0);
 //BA.debugLineNum = 534;BA.debugLine="ToastMessageShow(\"Hubo un error, intente de nuev";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Hubo un error, intente de nuevo"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 535;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 539;BA.debugLine="Main.Idproyecto = Main.currentproject";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvv3 /*int*/  = (int)(Double.parseDouble(mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv7 /*String*/ ));
 //BA.debugLineNum = 540;BA.debugLine="StartActivity(frmInstrucciones)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv6.getObject()));
 //BA.debugLineNum = 542;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim currentscreen As String";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv0 = "";
 //BA.debugLineNum = 18;BA.debugLine="Dim resetting_msg As Boolean";
_resetting_msg = false;
 //BA.debugLineNum = 21;BA.debugLine="Private tabStripMain As TabStrip";
mostCurrent._tabstripmain = new anywheresoftware.b4a.objects.TabStripViewPager();
 //BA.debugLineNum = 22;BA.debugLine="Private btnMenu As Button";
mostCurrent._btnmenu = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblEncontreMosquito As Label";
mostCurrent._lblencontremosquito = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private btnInformeMosquito As Button";
mostCurrent._btninformemosquito = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lblEncontreCriadero As Label";
mostCurrent._lblencontrecriadero = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private btnInformeCriadero As Button";
mostCurrent._btninformecriadero = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim p As Phone";
mostCurrent._vvvvvvv6 = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 34;BA.debugLine="Dim Drawer As B4XDrawer";
mostCurrent._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv5 = new caza.mosquito.b4xdrawer();
 //BA.debugLineNum = 35;BA.debugLine="Private btnComoFotosReportar As Label";
mostCurrent._btncomofotosreportar = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private btnCerrarSesion As Button";
mostCurrent._btncerrarsesion = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private btnEditUser As Label";
mostCurrent._btnedituser = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private btnDatosAnteriores As Label";
mostCurrent._btndatosanteriores = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblUserName As Label";
mostCurrent._lblusername = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private lblRegistrate As Label";
mostCurrent._lblregistrate = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private btnVerPerfil As Label";
mostCurrent._btnverperfil = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private btnAbout As Label";
mostCurrent._btnabout = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private btnPoliticaDatos As Label";
mostCurrent._btnpoliticadatos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private lblGrupo As Label";
mostCurrent._lblgrupo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private lblUserNameMain As Label";
mostCurrent._lblusernamemain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private lblGrupoMain As Label";
mostCurrent._lblgrupomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="End Sub";
return "";
}
public static String  _imglogo_click() throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub imgLogo_Click";
 //BA.debugLineNum = 172;BA.debugLine="StartActivity(frmabout)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv2.getObject()));
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _lblaccion_click() throws Exception{
 //BA.debugLineNum = 599;BA.debugLine="Sub lblAccion_Click";
 //BA.debugLineNum = 600;BA.debugLine="btnAccion_Click";
_btnaccion_click();
 //BA.debugLineNum = 601;BA.debugLine="End Sub";
return "";
}
public static String  _lblaprender_click() throws Exception{
 //BA.debugLineNum = 577;BA.debugLine="Sub lblAprender_Click";
 //BA.debugLineNum = 578;BA.debugLine="StartActivity(frmInfografias_Main)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._frminfografias_main.getObject()));
 //BA.debugLineNum = 579;BA.debugLine="End Sub";
return "";
}
public static String  _lblexplorarmapa_click() throws Exception{
 //BA.debugLineNum = 588;BA.debugLine="Sub lblExplorarMapa_Click";
 //BA.debugLineNum = 589;BA.debugLine="StartActivity(frmMapa)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvvv0.getObject()));
 //BA.debugLineNum = 590;BA.debugLine="End Sub";
return "";
}
public static String  _lblgrupo_click() throws Exception{
 //BA.debugLineNum = 656;BA.debugLine="Sub lblGrupo_Click";
 //BA.debugLineNum = 657;BA.debugLine="MsgboxAsync(\"¡Puedes formar parte de un grupo o c";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("¡Puedes formar parte de un grupo o colectivo de personas para coordinar tus esfuerzos!"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Unite a un grupo desde 'Mi perfil', en el menú lateral"),BA.ObjectToCharSequence("Unite a un grupo!"),processBA);
 //BA.debugLineNum = 658;BA.debugLine="End Sub";
return "";
}
public static String  _lblgrupomain_click() throws Exception{
 //BA.debugLineNum = 660;BA.debugLine="Sub lblGrupoMain_Click";
 //BA.debugLineNum = 661;BA.debugLine="MsgboxAsync(\"¡Puedes formar parte de un grupo o c";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("¡Puedes formar parte de un grupo o colectivo de personas para coordinar tus esfuerzos!"+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"Unite a un grupo desde 'Mi perfil', en el menú lateral"),BA.ObjectToCharSequence("Unite a un grupo!"),processBA);
 //BA.debugLineNum = 662;BA.debugLine="End Sub";
return "";
}
public static String  _lblinformecriadero_click() throws Exception{
 //BA.debugLineNum = 446;BA.debugLine="Sub lblInformeCriadero_Click";
 //BA.debugLineNum = 447;BA.debugLine="btnInformeCriadero_Click";
_btninformecriadero_click();
 //BA.debugLineNum = 448;BA.debugLine="End Sub";
return "";
}
public static String  _lblinformemosquito_click() throws Exception{
 //BA.debugLineNum = 411;BA.debugLine="Sub lblInformeMosquito_Click";
 //BA.debugLineNum = 412;BA.debugLine="btnInformeMosquito_Click";
_btninformemosquito_click();
 //BA.debugLineNum = 413;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="Dim fullidcurrentproject As String";
_v5 = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim rp As RuntimePermissions";
_v6 = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1() throws Exception{
caza.mosquito.downloadservice._downloaddata _dd = null;
 //BA.debugLineNum = 613;BA.debugLine="Sub ResetMessages";
 //BA.debugLineNum = 616;BA.debugLine="Dim dd As DownloadData";
_dd = new caza.mosquito.downloadservice._downloaddata();
 //BA.debugLineNum = 617;BA.debugLine="dd.url = Main.serverPath & \"/connect2/resetmessag";
_dd.url /*String*/  = mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv2 /*String*/ +"/connect2/resetmessages_new.php?deviceID="+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv1 /*String*/ ;
 //BA.debugLineNum = 618;BA.debugLine="dd.EventName = \"ResetMessages\"";
_dd.EventName /*String*/  = "ResetMessages";
 //BA.debugLineNum = 619;BA.debugLine="dd.Target = Me";
_dd.Target /*Object*/  = frmprincipal.getObject();
 //BA.debugLineNum = 620;BA.debugLine="CallSubDelayed2(DownloadService, \"StartDownload\",";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv0.getObject()),"StartDownload",(Object)(_dd));
 //BA.debugLineNum = 621;BA.debugLine="resetting_msg = True";
_resetting_msg = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 622;BA.debugLine="End Sub";
return "";
}
public static String  _resetmessages_complete(caza.mosquito.httpjob _job) throws Exception{
String _ret = "";
String _act = "";
anywheresoftware.b4a.objects.collections.JSONParser _parser = null;
 //BA.debugLineNum = 624;BA.debugLine="Sub ResetMessages_Complete(Job As HttpJob)";
 //BA.debugLineNum = 625;BA.debugLine="Log(\"Reset messages: \" & Job.Success)";
anywheresoftware.b4a.keywords.Common.LogImpl("52883585","Reset messages: "+BA.ObjectToString(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ),0);
 //BA.debugLineNum = 626;BA.debugLine="If Job.Success = True Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 627;BA.debugLine="Dim ret As String";
_ret = "";
 //BA.debugLineNum = 628;BA.debugLine="Dim act As String";
_act = "";
 //BA.debugLineNum = 629;BA.debugLine="ret = Job.GetString";
_ret = _job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ();
 //BA.debugLineNum = 630;BA.debugLine="Dim parser As JSONParser";
_parser = new anywheresoftware.b4a.objects.collections.JSONParser();
 //BA.debugLineNum = 631;BA.debugLine="parser.Initialize(ret)";
_parser.Initialize(_ret);
 //BA.debugLineNum = 632;BA.debugLine="act = parser.NextValue";
_act = BA.ObjectToString(_parser.NextValue());
 //BA.debugLineNum = 633;BA.debugLine="If act = \"Not Found\" Then";
if ((_act).equals("Not Found")) { 
 //BA.debugLineNum = 634;BA.debugLine="ToastMessageShow(\"Error\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Error"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("Error reseteando los mensajes, ")) { 
 //BA.debugLineNum = 636;BA.debugLine="ToastMessageShow(\"Login failed\", True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Login failed"),anywheresoftware.b4a.keywords.Common.True);
 }else if((_act).equals("ResetMessages")) { 
 };
 //BA.debugLineNum = 640;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 642;BA.debugLine="Log(\"reset messages not ok\")";
anywheresoftware.b4a.keywords.Common.LogImpl("52883602","reset messages not ok",0);
 //BA.debugLineNum = 643;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 644;BA.debugLine="MsgboxAsync(\"Al parecer hay un problema en nues";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("Al parecer hay un problema en nuestros servidores, lo solucionaremos pronto!"),BA.ObjectToCharSequence("Mala mía"),processBA);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 646;BA.debugLine="MsgboxAsync(\"There seems to be a problem with t";
anywheresoftware.b4a.keywords.Common.MsgboxAsync(BA.ObjectToCharSequence("There seems to be a problem with the server, we will fix it soon!"),BA.ObjectToCharSequence("My bad!"),processBA);
 };
 //BA.debugLineNum = 648;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 651;BA.debugLine="Job.Release";
_job._vvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ ();
 //BA.debugLineNum = 652;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6(boolean _online,boolean _firsttime) throws Exception{
String _msjpriv = "";
String[] _msjarray = null;
int _i = 0;
boolean _hayevals = false;
anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
String _msg = "";
 //BA.debugLineNum = 285;BA.debugLine="Sub startBienvienido(online As Boolean, firsttime";
 //BA.debugLineNum = 288;BA.debugLine="If online = True Then";
if (_online==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 292;BA.debugLine="If Main.msjprivadoleido = True Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv5 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 293;BA.debugLine="ResetMessages";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1();
 };
 //BA.debugLineNum = 297;BA.debugLine="If Main.msjprivadouser <> \"None\" And firsttime";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv4 /*String*/ ).equals("None") == false && _firsttime==anywheresoftware.b4a.keywords.Common.True && mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv5 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 298;BA.debugLine="Dim msjpriv As String";
_msjpriv = "";
 //BA.debugLineNum = 300;BA.debugLine="Dim msjarray() As String = Regex.Split(\"#\", Ma";
_msjarray = anywheresoftware.b4a.keywords.Common.Regex.Split("#",mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv4 /*String*/ );
 //BA.debugLineNum = 301;BA.debugLine="For i = 0 To msjarray.Length - 1";
{
final int step8 = 1;
final int limit8 = (int) (_msjarray.length-1);
_i = (int) (0) ;
for (;_i <= limit8 ;_i = _i + step8 ) {
 //BA.debugLineNum = 303;BA.debugLine="If msjarray(i) <> \"\" Then";
if ((_msjarray[_i]).equals("") == false) { 
 //BA.debugLineNum = 305;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 306;BA.debugLine="msjpriv = utilidades.Mensaje(\"Notificación\",";
_msjpriv = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Notificación","MsgMensaje.png","Notificación privada",_msjarray[_i],(Object)("Ok"),(Object)(""),(Object)(""),anywheresoftware.b4a.keywords.Common.True);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 308;BA.debugLine="msjpriv = utilidades.Mensaje(\"Notification\",";
_msjpriv = mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv6 /*String*/ (mostCurrent.activityBA,"Notification","MsgMensaje.png","Private notification",_msjarray[_i],(Object)("Ok"),(Object)(""),(Object)(""),anywheresoftware.b4a.keywords.Common.True);
 };
 };
 }
};
 //BA.debugLineNum = 316;BA.debugLine="If msjpriv = DialogResponse.POSITIVE Then";
if ((_msjpriv).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 };
 };
 //BA.debugLineNum = 329;BA.debugLine="Dim hayevals As Boolean";
_hayevals = false;
 //BA.debugLineNum = 330;BA.debugLine="Dim Cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 331;BA.debugLine="Cursor1 = Starter.SQLdb.ExecQuery(\"SELECT * FROM";
_cursor1 = (anywheresoftware.b4a.sql.SQL.CursorWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.sql.SQL.CursorWrapper(), (android.database.Cursor)(mostCurrent._vvvvvvvvvvvvvvvvvv4._v0 /*anywheresoftware.b4a.sql.SQL*/ .ExecQuery("SELECT * FROM markers_local WHERE usuario='"+mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/ +"' AND evalsent='no'")));
 //BA.debugLineNum = 332;BA.debugLine="If Cursor1.RowCount > 0 Then";
if (_cursor1.getRowCount()>0) { 
 //BA.debugLineNum = 333;BA.debugLine="hayevals = True";
_hayevals = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 335;BA.debugLine="Cursor1.Close";
_cursor1.Close();
 //BA.debugLineNum = 339;BA.debugLine="If hayevals = True And firsttime = True Then";
if (_hayevals==anywheresoftware.b4a.keywords.Common.True && _firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 341;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 342;BA.debugLine="Dim msg As String = Msgbox2(\"Tiene reportes s";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Tiene reportes sin enviar. Desea hacerlo ahora?"),BA.ObjectToCharSequence("Atención"),"Si, los enviaré ahora","","No, las enviaré luego",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 344;BA.debugLine="Dim msg As String = Msgbox2(\"You have unsent";
_msg = BA.NumberToString(anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("You have unsent reports, do you want to send them now?"),BA.ObjectToCharSequence("Warning"),"Yes, I'll send them now","","No, I'll send them later",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 };
 //BA.debugLineNum = 348;BA.debugLine="If msg=DialogResponse.POSITIVE Then";
if ((_msg).equals(BA.NumberToString(anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE))) { 
 //BA.debugLineNum = 349;BA.debugLine="If Main.modooffline = False Then";
if (mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/ ==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 350;BA.debugLine="frmDatosAnteriores.hayAnteriores = True";
mostCurrent._vvvvvvvvvvvvvvvvvvv6._vvvvvvv5 /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 351;BA.debugLine="StartActivity(frmDatosAnteriores)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv6.getObject()));
 }else {
 //BA.debugLineNum = 353;BA.debugLine="If Main.lang = \"es\" Then";
if ((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("es")) { 
 //BA.debugLineNum = 354;BA.debugLine="ToastMessageShow(\"No ha iniciado sesión aún";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("No ha iniciado sesión aún"),anywheresoftware.b4a.keywords.Common.False);
 }else if((mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvvv5 /*String*/ ).equals("en")) { 
 //BA.debugLineNum = 356;BA.debugLine="ToastMessageShow(\"You haven't logged in yet";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("You haven't logged in yet"),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 };
 };
 }else {
 //BA.debugLineNum = 365;BA.debugLine="Main.modooffline = True";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvvv1 /*boolean*/  = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 366;BA.debugLine="resetting_msg = False";
_resetting_msg = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
return "";
}
}
