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

public class frmelmosquito extends Activity implements B4AActivity{
	public static frmelmosquito mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmelmosquito");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmelmosquito).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmelmosquito");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmelmosquito", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmelmosquito) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmelmosquito) Resume **");
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
		return frmelmosquito.class;
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
            BA.LogInfo("** Activity (frmelmosquito) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmelmosquito) Pause event (activity is not paused). **");
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
            frmelmosquito mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmelmosquito) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _timeranimacion = null;
public static anywheresoftware.b4a.objects.Timer _timeranimacionentrada = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butcolor = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butpatas = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquito = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltitulomosquito = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsubtitulomosquito = null;
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scrollextramosquito = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelpopups_1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfondo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpopup_titulo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpopup_descripcion = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerrarpopup = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgpopup = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butpaso4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso3a = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgpupas = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso2a = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imglarvas = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butpaso2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butpaso3 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquito1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imghuevos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso2b = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butpaso1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelpopups_2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblestadio = null;
public anywheresoftware.b4a.objects.ButtonWrapper _but_cerrar = null;
public caza.mosquito.main _main = null;
public caza.mosquito.frmcamara _frmcamara = null;
public caza.mosquito.frmquehacer _frmquehacer = null;
public caza.mosquito.register _register = null;
public caza.mosquito.frmlogin _frmlogin = null;
public caza.mosquito.frmprincipal _frmprincipal = null;
public caza.mosquito.starter _starter = null;
public caza.mosquito.dbutils _dbutils = null;
public caza.mosquito.downloadservice _downloadservice = null;
public caza.mosquito.firebasemessaging _firebasemessaging = null;
public caza.mosquito.frmabout _frmabout = null;
public caza.mosquito.frmaprender _frmaprender = null;
public caza.mosquito.frmcomofotos _frmcomofotos = null;
public caza.mosquito.frmcomotransmiten _frmcomotransmiten = null;
public caza.mosquito.frmcomovemos_app _frmcomovemos_app = null;
public caza.mosquito.frmcomovemos_enfermedades _frmcomovemos_enfermedades = null;
public caza.mosquito.frmcomovemos_porqueexiste _frmcomovemos_porqueexiste = null;
public caza.mosquito.frmdatosanteriores _frmdatosanteriores = null;
public caza.mosquito.frmdondecria _frmdondecria = null;
public caza.mosquito.frmeditprofile _frmeditprofile = null;
public caza.mosquito.frmenfermedades _frmenfermedades = null;
public caza.mosquito.frmfotos _frmfotos = null;
public caza.mosquito.frmfotoscriadero _frmfotoscriadero = null;
public caza.mosquito.frmidentificarmosquito _frmidentificarmosquito = null;
public caza.mosquito.frminfografias_main _frminfografias_main = null;
public caza.mosquito.frminstrucciones _frminstrucciones = null;
public caza.mosquito.frmlocalizacion _frmlocalizacion = null;
public caza.mosquito.frmmapa _frmmapa = null;
public caza.mosquito.frmpoliticadatos _frmpoliticadatos = null;
public caza.mosquito.httputils2service _httputils2service = null;
public caza.mosquito.multipartpost _multipartpost = null;
public caza.mosquito.uploadfiles _uploadfiles = null;
public caza.mosquito.utilidades _utilidades = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 59;BA.debugLine="Activity.LoadLayout(\"lay_mosquito_Main\")";
mostCurrent._activity.LoadLayout("lay_mosquito_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 61;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarpopup_click() throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub btnCerrarPopUp_Click";
 //BA.debugLineNum = 116;BA.debugLine="panelPopUps_1.RemoveView";
mostCurrent._panelpopups_1.RemoveView();
 //BA.debugLineNum = 117;BA.debugLine="butPatas.Visible = True";
mostCurrent._butpatas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="butColor.Visible = True";
mostCurrent._butcolor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 119;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarpopup_huevos_click() throws Exception{
 //BA.debugLineNum = 408;BA.debugLine="Sub btnCerrarPopUp_Huevos_Click";
 //BA.debugLineNum = 409;BA.debugLine="panelPopUps_2.RemoveView";
mostCurrent._panelpopups_2.RemoveView();
 //BA.debugLineNum = 410;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 411;BA.debugLine="butPaso1.Visible = False";
mostCurrent._butpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 412;BA.debugLine="butPaso2.Visible = True";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 413;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 414;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 415;BA.debugLine="lblLabelPaso1.Visible = True";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 416;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 417;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 418;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 419;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 420;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 421;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 422;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 423;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 424;BA.debugLine="imgMosquito1.Visible = True";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 425;BA.debugLine="imgHuevos.Visible = True";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 426;BA.debugLine="lblEstadio.Visible = True";
mostCurrent._lblestadio.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 428;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarpopup_larvas_click() throws Exception{
 //BA.debugLineNum = 388;BA.debugLine="Sub btnCerrarPopUp_Larvas_Click";
 //BA.debugLineNum = 389;BA.debugLine="panelPopUps_2.RemoveView";
mostCurrent._panelpopups_2.RemoveView();
 //BA.debugLineNum = 390;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 391;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 392;BA.debugLine="butPaso3.Visible = True";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 393;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 394;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 395;BA.debugLine="lblPaso2a.Visible = True";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 396;BA.debugLine="lblPaso2b.Visible = True";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 397;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 398;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 399;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 400;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 401;BA.debugLine="imgLarvas.Visible = True";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 402;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 403;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 404;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 405;BA.debugLine="lblEstadio.Visible = True";
mostCurrent._lblestadio.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 407;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarpopup_pupas_click() throws Exception{
 //BA.debugLineNum = 429;BA.debugLine="Sub btnCerrarPopUp_Pupas_Click";
 //BA.debugLineNum = 430;BA.debugLine="panelPopUps_2.RemoveView";
mostCurrent._panelpopups_2.RemoveView();
 //BA.debugLineNum = 431;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 432;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 433;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 434;BA.debugLine="butPaso4.Visible = True";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 435;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 436;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 437;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 438;BA.debugLine="lblPaso3.Visible = True";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 439;BA.debugLine="lblPaso3a.Visible = True";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 440;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 441;BA.debugLine="imgPupas.Visible = True";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 442;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 443;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 444;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 445;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 447;BA.debugLine="End Sub";
return "";
}
public static String  _btnfrasco_click() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub btnFrasco_Click";
 //BA.debugLineNum = 125;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 126;BA.debugLine="Activity.LoadLayout(\"lay_mosquito_Ciclo\")";
mostCurrent._activity.LoadLayout("lay_mosquito_Ciclo",mostCurrent.activityBA);
 //BA.debugLineNum = 128;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPaso1, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpaso1,anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _btnmosquito_click() throws Exception{
 //BA.debugLineNum = 73;BA.debugLine="Sub btnMosquito_Click";
 //BA.debugLineNum = 74;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 75;BA.debugLine="Activity.LoadLayout(\"lay_mosquito_Mosquito\")";
mostCurrent._activity.LoadLayout("lay_mosquito_Mosquito",mostCurrent.activityBA);
 //BA.debugLineNum = 76;BA.debugLine="scrollExtraMosquito.Panel.LoadLayout(\"lay_mosquit";
mostCurrent._scrollextramosquito.getPanel().LoadLayout("lay_mosquito_Paneles",mostCurrent.activityBA);
 //BA.debugLineNum = 77;BA.debugLine="scrollExtraMosquito.Panel.Width = 600dip";
mostCurrent._scrollextramosquito.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (600)));
 //BA.debugLineNum = 78;BA.debugLine="scrollExtraMosquito.FullScroll(True)";
mostCurrent._scrollextramosquito.FullScroll(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 79;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPatas, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpatas,anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 80;BA.debugLine="utilidades.CreateHaloEffect(Activity, butColor, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butcolor,anywheresoftware.b4a.keywords.Common.Colors.Yellow);
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_ciclo_click() throws Exception{
 //BA.debugLineNum = 452;BA.debugLine="Sub but_Cerrar_Ciclo_Click";
 //BA.debugLineNum = 453;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 454;BA.debugLine="Activity.LoadLayout(\"lay_mosquito_Main\")";
mostCurrent._activity.LoadLayout("lay_mosquito_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 455;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_click() throws Exception{
 //BA.debugLineNum = 448;BA.debugLine="Sub but_Cerrar_Click";
 //BA.debugLineNum = 449;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 450;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 451;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_mosquito_click() throws Exception{
 //BA.debugLineNum = 456;BA.debugLine="Sub but_Cerrar_Mosquito_Click";
 //BA.debugLineNum = 457;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 458;BA.debugLine="Activity.LoadLayout(\"lay_mosquito_Main\")";
mostCurrent._activity.LoadLayout("lay_mosquito_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static String  _butcolor_click() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub butColor_Click";
 //BA.debugLineNum = 100;BA.debugLine="butPatas.Visible = False";
mostCurrent._butpatas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 101;BA.debugLine="butColor.Visible = False";
mostCurrent._butcolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 103;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 104;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 105;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 107;BA.debugLine="panelPopUps_1.Initialize(\"\")";
mostCurrent._panelpopups_1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 108;BA.debugLine="panelPopUps_1.LoadLayout(\"lay_Mosquito_PopUps\")";
mostCurrent._panelpopups_1.LoadLayout("lay_Mosquito_PopUps",mostCurrent.activityBA);
 //BA.debugLineNum = 110;BA.debugLine="lblPopUp_Descripcion.Text = \"A simple vista se ve";
mostCurrent._lblpopup_descripcion.setText(BA.ObjectToCharSequence("A simple vista se ve"+anywheresoftware.b4a.keywords.Common.CRLF+"de color negro intenso"));
 //BA.debugLineNum = 111;BA.debugLine="imgPopUp.Bitmap = LoadBitmap(File.DirAssets,\"mosq";
mostCurrent._imgpopup.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mosquito_Color.png").getObject()));
 //BA.debugLineNum = 112;BA.debugLine="imgPopUp.Visible = True";
mostCurrent._imgpopup.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 113;BA.debugLine="Activity.AddView(panelPopUps_1, 15%x, 15%y, 70%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelpopups_1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public static String  _butpaso1_click() throws Exception{
 //BA.debugLineNum = 131;BA.debugLine="Sub butPaso1_Click";
 //BA.debugLineNum = 133;BA.debugLine="If imgMosquito.Visible = True Then";
if (mostCurrent._imgmosquito.getVisible()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 134;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 135;BA.debugLine="TimerAnimacion.Initialize(\"TimerAnimacion\", 10)";
_timeranimacion.Initialize(processBA,"TimerAnimacion",(long) (10));
 //BA.debugLineNum = 136;BA.debugLine="TimerAnimacion.Enabled = True";
_timeranimacion.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 138;BA.debugLine="imgMosquito1.Left = -60dip";
mostCurrent._imgmosquito1.setLeft((int) (-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))));
 //BA.debugLineNum = 139;BA.debugLine="imgMosquito1.Top = 50dip";
mostCurrent._imgmosquito1.setTop(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 140;BA.debugLine="imgMosquito1.Visible = True";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 141;BA.debugLine="TimerAnimacionEntrada.Initialize(\"TimerAnimacion";
_timeranimacionentrada.Initialize(processBA,"TimerAnimacion_Entrada",(long) (10));
 //BA.debugLineNum = 142;BA.debugLine="TimerAnimacionEntrada.Enabled = True";
_timeranimacionentrada.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 149;BA.debugLine="End Sub";
return "";
}
public static String  _butpaso2_click() throws Exception{
 //BA.debugLineNum = 150;BA.debugLine="Sub butPaso2_Click";
 //BA.debugLineNum = 151;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 152;BA.debugLine="butPaso3.Visible = True";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 153;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 154;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 155;BA.debugLine="lblPaso2a.Visible = True";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 156;BA.debugLine="lblPaso2b.Visible = True";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 157;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 158;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 159;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="imgLarvas.Visible = True";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 162;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 164;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 165;BA.debugLine="lblEstadio.Text = \"Larva\"";
mostCurrent._lblestadio.setText(BA.ObjectToCharSequence("Larva"));
 //BA.debugLineNum = 166;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPaso3, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpaso3,anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 168;BA.debugLine="End Sub";
return "";
}
public static String  _butpaso3_click() throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub butPaso3_Click";
 //BA.debugLineNum = 170;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 172;BA.debugLine="butPaso4.Visible = True";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 173;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 174;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 175;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 176;BA.debugLine="lblPaso3.Visible = True";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 177;BA.debugLine="lblPaso3a.Visible = True";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 178;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 179;BA.debugLine="imgPupas.Visible = True";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 180;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 181;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 182;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 183;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 184;BA.debugLine="lblEstadio.Text = \"Pupa\"";
mostCurrent._lblestadio.setText(BA.ObjectToCharSequence("Pupa"));
 //BA.debugLineNum = 185;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPaso4, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpaso4,anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _butpaso4_click() throws Exception{
 //BA.debugLineNum = 187;BA.debugLine="Sub butPaso4_Click";
 //BA.debugLineNum = 188;BA.debugLine="butPaso1.Visible = True";
mostCurrent._butpaso1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 190;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 191;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 192;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 193;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 194;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 195;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 196;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 197;BA.debugLine="lblPaso4.Visible = True";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 198;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 199;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 200;BA.debugLine="imgMosquito.Visible = True";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 201;BA.debugLine="imgMosquito.Top = 170dip";
mostCurrent._imgmosquito.setTop(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)));
 //BA.debugLineNum = 202;BA.debugLine="imgMosquito.Left = 30dip";
mostCurrent._imgmosquito.setLeft(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)));
 //BA.debugLineNum = 203;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 204;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="lblEstadio.Text = \"Mosquito\"";
mostCurrent._lblestadio.setText(BA.ObjectToCharSequence("Mosquito"));
 //BA.debugLineNum = 206;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPaso1, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpaso1,anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 207;BA.debugLine="End Sub";
return "";
}
public static String  _butpatas_click() throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub butPatas_Click";
 //BA.debugLineNum = 84;BA.debugLine="butPatas.Visible = False";
mostCurrent._butpatas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 85;BA.debugLine="butColor.Visible = False";
mostCurrent._butcolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 88;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 89;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 91;BA.debugLine="panelPopUps_1.Initialize(\"\")";
mostCurrent._panelpopups_1.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 92;BA.debugLine="panelPopUps_1.LoadLayout(\"lay_Mosquito_PopUps\")";
mostCurrent._panelpopups_1.LoadLayout("lay_Mosquito_PopUps",mostCurrent.activityBA);
 //BA.debugLineNum = 94;BA.debugLine="lblPopUp_Descripcion.Text = \"Se pueden ver rayas";
mostCurrent._lblpopup_descripcion.setText(BA.ObjectToCharSequence("Se pueden ver rayas blancas en sus patas"));
 //BA.debugLineNum = 95;BA.debugLine="imgPopUp.Bitmap = LoadBitmap(File.DirAssets,\"mosq";
mostCurrent._imgpopup.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mosquito_Grande.png").getObject()));
 //BA.debugLineNum = 96;BA.debugLine="imgPopUp.Visible = True";
mostCurrent._imgpopup.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 97;BA.debugLine="Activity.AddView(panelPopUps_1, 15%x, 15%y, 70%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelpopups_1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private butColor As Button";
mostCurrent._butcolor = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private butPatas As Button";
mostCurrent._butpatas = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private imgMosquito As ImageView";
mostCurrent._imgmosquito = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private lblTituloMosquito As Label";
mostCurrent._lbltitulomosquito = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private lblSubTituloMosquito As Label";
mostCurrent._lblsubtitulomosquito = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private scrollExtraMosquito As HorizontalScrollVi";
mostCurrent._scrollextramosquito = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private panelPopUps_1 As Panel";
mostCurrent._panelpopups_1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblFondo As Label";
mostCurrent._lblfondo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblPopUp_titulo As Label";
mostCurrent._lblpopup_titulo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblPopUp_Descripcion As Label";
mostCurrent._lblpopup_descripcion = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private btnCerrarPopUp As Button";
mostCurrent._btncerrarpopup = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private imgPopUp As ImageView";
mostCurrent._imgpopup = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lblPaso4 As Label";
mostCurrent._lblpaso4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private butPaso4 As Button";
mostCurrent._butpaso4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private lblLabelPaso1 As Label";
mostCurrent._lbllabelpaso1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private lblPaso3a As Label";
mostCurrent._lblpaso3a = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private imgPupas As ImageView";
mostCurrent._imgpupas = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lblPaso3 As Label";
mostCurrent._lblpaso3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private lblPaso2a As Label";
mostCurrent._lblpaso2a = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private imgLarvas As ImageView";
mostCurrent._imglarvas = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private butPaso2 As Button";
mostCurrent._butpaso2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private butPaso3 As Button";
mostCurrent._butpaso3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private imgMosquito1 As ImageView";
mostCurrent._imgmosquito1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private imgHuevos As ImageView";
mostCurrent._imghuevos = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblPaso2b As Label";
mostCurrent._lblpaso2b = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private butPaso1 As Button";
mostCurrent._butpaso1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private panelPopUps_2 As Panel";
mostCurrent._panelpopups_2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private lblEstadio As Label";
mostCurrent._lblestadio = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private but_Cerrar As Button";
mostCurrent._but_cerrar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static String  _imghuevos_click() throws Exception{
 //BA.debugLineNum = 250;BA.debugLine="Sub imgHuevos_Click";
 //BA.debugLineNum = 291;BA.debugLine="End Sub";
return "";
}
public static String  _imglarvas_click() throws Exception{
 //BA.debugLineNum = 209;BA.debugLine="Sub imgLarvas_Click";
 //BA.debugLineNum = 210;BA.debugLine="butPaso2.Visible = False";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 211;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 212;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 213;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 214;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 215;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 216;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 217;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 218;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 219;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 221;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 222;BA.debugLine="imgMosquito1.Visible = False";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 223;BA.debugLine="imgHuevos.Visible = False";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 224;BA.debugLine="lblEstadio.Visible = False";
mostCurrent._lblestadio.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 226;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 227;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 228;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 230;BA.debugLine="panelPopUps_2.Initialize(\"\")";
mostCurrent._panelpopups_2.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 231;BA.debugLine="panelPopUps_2.LoadLayout(\"lay_Mosquito_PopUps\")";
mostCurrent._panelpopups_2.LoadLayout("lay_Mosquito_PopUps",mostCurrent.activityBA);
 //BA.debugLineNum = 233;BA.debugLine="lblPopUp_Descripcion.Text = \"\"";
mostCurrent._lblpopup_descripcion.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 234;BA.debugLine="imgPopUp.Visible = True";
mostCurrent._imgpopup.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 235;BA.debugLine="imgPopUp.RemoveView";
mostCurrent._imgpopup.RemoveView();
 //BA.debugLineNum = 236;BA.debugLine="imgPopUp.Initialize(\"\")";
mostCurrent._imgpopup.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 237;BA.debugLine="imgPopUp.Bitmap = LoadBitmap(File.DirAssets, \"mos";
mostCurrent._imgpopup.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mosquito_larvaFoto.png").getObject()));
 //BA.debugLineNum = 238;BA.debugLine="imgPopUp.Gravity = Gravity.FILL";
mostCurrent._imgpopup.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 239;BA.debugLine="btnCerrarPopUp.RemoveView";
mostCurrent._btncerrarpopup.RemoveView();
 //BA.debugLineNum = 240;BA.debugLine="btnCerrarPopUp.Initialize(\"btnCerrarPopUp_Larvas\"";
mostCurrent._btncerrarpopup.Initialize(mostCurrent.activityBA,"btnCerrarPopUp_Larvas");
 //BA.debugLineNum = 241;BA.debugLine="btnCerrarPopUp.Typeface = Typeface.FONTAWESOME";
mostCurrent._btncerrarpopup.setTypeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME());
 //BA.debugLineNum = 242;BA.debugLine="btnCerrarPopUp.Text = \"\"";
mostCurrent._btncerrarpopup.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 243;BA.debugLine="btnCerrarPopUp.TextSize = 30";
mostCurrent._btncerrarpopup.setTextSize((float) (30));
 //BA.debugLineNum = 244;BA.debugLine="btnCerrarPopUp.Color = Colors.ARGB(150,255,255,25";
mostCurrent._btncerrarpopup.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 245;BA.debugLine="btnCerrarPopUp.TextColor = Colors.ARGB(255,255,11";
mostCurrent._btncerrarpopup.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (117),(int) (117)));
 //BA.debugLineNum = 246;BA.debugLine="panelPopUps_2.AddView(imgPopUp, 0%x, 0%y, 70%x, 7";
mostCurrent._panelpopups_2.AddView((android.view.View)(mostCurrent._imgpopup.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 247;BA.debugLine="panelPopUps_2.AddView(btnCerrarPopUp, 56%x, 0%y,";
mostCurrent._panelpopups_2.AddView((android.view.View)(mostCurrent._btncerrarpopup.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (56),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 248;BA.debugLine="Activity.AddView(panelPopUps_2, 15%x, 15%y, 70%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelpopups_2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (15),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (70),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _imgpupas_click() throws Exception{
 //BA.debugLineNum = 292;BA.debugLine="Sub imgPupas_Click";
 //BA.debugLineNum = 332;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Private TimerAnimacion As Timer";
_timeranimacion = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="Private TimerAnimacionEntrada As Timer";
_timeranimacionentrada = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _timeranimacion_entrada_tick() throws Exception{
 //BA.debugLineNum = 354;BA.debugLine="Sub TimerAnimacion_Entrada_Tick";
 //BA.debugLineNum = 355;BA.debugLine="If imgMosquito1.left < 220dip Or imgMosquito1.Top";
if (mostCurrent._imgmosquito1.getLeft()<anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (220)) || mostCurrent._imgmosquito1.getTop()<anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (340))) { 
 //BA.debugLineNum = 356;BA.debugLine="imgMosquito1.Left = imgMosquito1.Left + 20";
mostCurrent._imgmosquito1.setLeft((int) (mostCurrent._imgmosquito1.getLeft()+20));
 //BA.debugLineNum = 357;BA.debugLine="imgMosquito1.Top = imgMosquito1.Top + 20";
mostCurrent._imgmosquito1.setTop((int) (mostCurrent._imgmosquito1.getTop()+20));
 }else {
 //BA.debugLineNum = 359;BA.debugLine="TimerAnimacionEntrada.Enabled = False";
_timeranimacionentrada.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 364;BA.debugLine="butPaso1.Visible = False";
mostCurrent._butpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 365;BA.debugLine="butPaso2.Visible = True";
mostCurrent._butpaso2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 366;BA.debugLine="butPaso3.Visible = False";
mostCurrent._butpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 367;BA.debugLine="butPaso4.Visible = False";
mostCurrent._butpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 368;BA.debugLine="lblLabelPaso1.Visible = True";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 369;BA.debugLine="lblPaso2a.Visible = False";
mostCurrent._lblpaso2a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 370;BA.debugLine="lblPaso2b.Visible = False";
mostCurrent._lblpaso2b.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 371;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 372;BA.debugLine="lblPaso3a.Visible = False";
mostCurrent._lblpaso3a.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 373;BA.debugLine="lblPaso4.Visible = False";
mostCurrent._lblpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 374;BA.debugLine="imgPupas.Visible = False";
mostCurrent._imgpupas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 375;BA.debugLine="imgLarvas.Visible = False";
mostCurrent._imglarvas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 376;BA.debugLine="imgMosquito.Visible = False";
mostCurrent._imgmosquito.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 377;BA.debugLine="imgMosquito1.Visible = True";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 378;BA.debugLine="imgHuevos.Visible = True";
mostCurrent._imghuevos.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 379;BA.debugLine="lblEstadio.Visible = True";
mostCurrent._lblestadio.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 380;BA.debugLine="lblEstadio.Text = \"Huevo\"";
mostCurrent._lblestadio.setText(BA.ObjectToCharSequence("Huevo"));
 //BA.debugLineNum = 381;BA.debugLine="utilidades.CreateHaloEffect(Activity, butPaso2,";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butpaso2,anywheresoftware.b4a.keywords.Common.Colors.Red);
 };
 //BA.debugLineNum = 384;BA.debugLine="End Sub";
return "";
}
public static String  _timeranimacion_tick() throws Exception{
 //BA.debugLineNum = 336;BA.debugLine="Sub TimerAnimacion_Tick";
 //BA.debugLineNum = 337;BA.debugLine="If imgMosquito.Left > 0 Or imgMosquito.Top > 0 Th";
if (mostCurrent._imgmosquito.getLeft()>0 || mostCurrent._imgmosquito.getTop()>0) { 
 //BA.debugLineNum = 338;BA.debugLine="imgMosquito.Left = imgMosquito.Left - 15";
mostCurrent._imgmosquito.setLeft((int) (mostCurrent._imgmosquito.getLeft()-15));
 //BA.debugLineNum = 339;BA.debugLine="imgMosquito.Top = imgMosquito.Top - 15";
mostCurrent._imgmosquito.setTop((int) (mostCurrent._imgmosquito.getTop()-15));
 }else {
 //BA.debugLineNum = 341;BA.debugLine="TimerAnimacion.Enabled = False";
_timeranimacion.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="butPaso1.Visible = False";
mostCurrent._butpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 344;BA.debugLine="imgMosquito1.Left = -60dip";
mostCurrent._imgmosquito1.setLeft((int) (-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))));
 //BA.debugLineNum = 345;BA.debugLine="imgMosquito1.Top = 50dip";
mostCurrent._imgmosquito1.setTop(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 346;BA.debugLine="imgMosquito1.Visible = True";
mostCurrent._imgmosquito1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 347;BA.debugLine="TimerAnimacionEntrada.Initialize(\"TimerAnimacion";
_timeranimacionentrada.Initialize(processBA,"TimerAnimacion_Entrada",(long) (10));
 //BA.debugLineNum = 348;BA.debugLine="TimerAnimacionEntrada.Enabled = True";
_timeranimacionentrada.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
return "";
}
}
