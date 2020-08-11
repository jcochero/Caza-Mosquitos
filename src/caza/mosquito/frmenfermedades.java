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

public class frmenfermedades extends Activity implements B4AActivity{
	public static frmenfermedades mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmenfermedades");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmenfermedades).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmenfermedades");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmenfermedades", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmenfermedades) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmenfermedades) Resume **");
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
		return frmenfermedades.class;
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
            BA.LogInfo("** Activity (frmenfermedades) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmenfermedades) Pause event (activity is not paused). **");
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
            frmenfermedades mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmenfermedades) Resume **");
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
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _scrollextrainfo = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imghombre = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butdolor = null;
public anywheresoftware.b4a.objects.ButtonWrapper _buterupciones = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butnauseas = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butfiebre = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltitulomain = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsubtitulomain = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butdengue = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butzika = null;
public anywheresoftware.b4a.objects.ButtonWrapper _butchicungunya = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsintoma_titulo = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgsintoma = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsintoma_descripcion = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btncerrarsintoma = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelsintoma = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblfondo = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcriticos_texto = null;
public anywheresoftware.b4a.objects.ButtonWrapper _but_backcriticos = null;
public anywheresoftware.b4a.objects.PanelWrapper _panelcriticos = null;
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
public caza.mosquito.frmelmosquito _frmelmosquito = null;
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
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 44;BA.debugLine="Activity.LoadLayout(\"lay_Enfermedades_Main\")";
mostCurrent._activity.LoadLayout("lay_Enfermedades_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 47;BA.debugLine="scrollExtraInfo.Panel.LoadLayout(\"lay_Enfermedade";
mostCurrent._scrollextrainfo.getPanel().LoadLayout("lay_Enfermedades_Paneles",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="scrollExtraInfo.Panel.Width = 1400dip";
mostCurrent._scrollextrainfo.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1400)));
 //BA.debugLineNum = 49;BA.debugLine="scrollExtraInfo.FullScroll(True)";
mostCurrent._scrollextrainfo.FullScroll(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 51;BA.debugLine="utilidades.CreateHaloEffect(Activity, butFiebre,";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butfiebre,anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 52;BA.debugLine="utilidades.CreateHaloEffect(Activity, butNauseas,";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butnauseas,anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 53;BA.debugLine="utilidades.CreateHaloEffect(Activity, butErupcion";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._buterupciones,anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 54;BA.debugLine="utilidades.CreateHaloEffect(Activity, butDolor, C";
mostCurrent._utilidades._createhaloeffect /*void*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.B4XViewWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.B4XViewWrapper(), (java.lang.Object)(mostCurrent._activity.getObject())),mostCurrent._butdolor,anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (140),(int) (0)));
 //BA.debugLineNum = 55;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 63;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public static String  _btncerrarsintoma_click() throws Exception{
 //BA.debugLineNum = 218;BA.debugLine="Sub btnCerrarSintoma_Click";
 //BA.debugLineNum = 219;BA.debugLine="panelSintoma.RemoveView";
mostCurrent._panelsintoma.RemoveView();
 //BA.debugLineNum = 220;BA.debugLine="butFiebre.Visible = True";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 221;BA.debugLine="butNauseas.Visible = True";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 222;BA.debugLine="butZika.Visible = True";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 223;BA.debugLine="butErupciones.Visible = True";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 224;BA.debugLine="butDolor.Visible = True";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 225;BA.debugLine="butChicungunya.Visible = True";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 226;BA.debugLine="butDengue.Visible = True";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 227;BA.debugLine="but_Cerrar.Visible = True";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 228;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
public static String  _but_backcriticos_click() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub but_BackCriticos_Click";
 //BA.debugLineNum = 126;BA.debugLine="panelCriticos.RemoveView";
mostCurrent._panelcriticos.RemoveView();
 //BA.debugLineNum = 127;BA.debugLine="butFiebre.Visible = True";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 128;BA.debugLine="butNauseas.Visible = True";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 129;BA.debugLine="butZika.Visible = True";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 130;BA.debugLine="butErupciones.Visible = True";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 131;BA.debugLine="butDolor.Visible = True";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="butChicungunya.Visible = True";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 133;BA.debugLine="butDengue.Visible = True";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 134;BA.debugLine="but_Cerrar.Visible = True";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 135;BA.debugLine="lblFondo.RemoveView";
mostCurrent._lblfondo.RemoveView();
 //BA.debugLineNum = 136;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_click() throws Exception{
 //BA.debugLineNum = 235;BA.debugLine="Sub but_Cerrar_Click";
 //BA.debugLineNum = 236;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 237;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static String  _butchicungunya_click() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub butChicungunya_Click";
 //BA.debugLineNum = 70;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 71;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 72;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 73;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 74;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 75;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 76;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 77;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 78;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 79;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 80;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 82;BA.debugLine="panelCriticos.Initialize(\"\")";
mostCurrent._panelcriticos.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 83;BA.debugLine="Activity.AddView(panelCriticos, 0,0, 100%x, 100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelcriticos.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 84;BA.debugLine="panelCriticos.LoadLayout(\"lay_Enfermedades_Critic";
mostCurrent._panelcriticos.LoadLayout("lay_Enfermedades_Criticos",mostCurrent.activityBA);
 //BA.debugLineNum = 85;BA.debugLine="lblCriticos_texto.Text = \"En los casos más graves";
mostCurrent._lblcriticos_texto.setText(BA.ObjectToCharSequence("En los casos más graves (y menos frecuentes), los dolores articulares y musculares pueden persistir por meses o años y existen algunos reportes de problemas neurológicos y cardíacos"));
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static String  _butdengue_click() throws Exception{
 //BA.debugLineNum = 107;BA.debugLine="Sub butDengue_Click";
 //BA.debugLineNum = 108;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 109;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 110;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 111;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 112;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 113;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 114;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 115;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 117;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 118;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 120;BA.debugLine="panelCriticos.Initialize(\"\")";
mostCurrent._panelcriticos.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 121;BA.debugLine="Activity.AddView(panelCriticos, 0,0, 100%x, 100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelcriticos.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 122;BA.debugLine="panelCriticos.LoadLayout(\"lay_Enfermedades_Critic";
mostCurrent._panelcriticos.LoadLayout("lay_Enfermedades_Criticos",mostCurrent.activityBA);
 //BA.debugLineNum = 123;BA.debugLine="lblCriticos_texto.Text = \"En las formas más grave";
mostCurrent._lblcriticos_texto.setText(BA.ObjectToCharSequence("En las formas más graves los casos pueden"+anywheresoftware.b4a.keywords.Common.CRLF+"presentar hemorragias e incluso"+anywheresoftware.b4a.keywords.Common.CRLF+"conducir a la muerte"));
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _butdolor_click() throws Exception{
 //BA.debugLineNum = 199;BA.debugLine="Sub butDolor_Click";
 //BA.debugLineNum = 200;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 201;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 202;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 204;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 205;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 206;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 207;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 208;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 209;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 210;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 211;BA.debugLine="panelSintoma.Initialize(\"\")";
mostCurrent._panelsintoma.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 212;BA.debugLine="panelSintoma.LoadLayout(\"lay_Enfermedades_Sintoma";
mostCurrent._panelsintoma.LoadLayout("lay_Enfermedades_Sintomas",mostCurrent.activityBA);
 //BA.debugLineNum = 213;BA.debugLine="lblSintoma_titulo.Text = \"DOLOR\"";
mostCurrent._lblsintoma_titulo.setText(BA.ObjectToCharSequence("DOLOR"));
 //BA.debugLineNum = 214;BA.debugLine="lblSintoma_Descripcion.Text = \"Muscular / De cabe";
mostCurrent._lblsintoma_descripcion.setText(BA.ObjectToCharSequence("Muscular / De cabeza"));
 //BA.debugLineNum = 215;BA.debugLine="imgSintoma.Bitmap = LoadBitmap(File.DirAssets, \"e";
mostCurrent._imgsintoma.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"enfermedades_dolor.png").getObject()));
 //BA.debugLineNum = 216;BA.debugLine="Activity.AddView(panelSintoma, 10%x, 10%y, 80%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelsintoma.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
public static String  _buterupciones_click() throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Sub butErupciones_Click";
 //BA.debugLineNum = 180;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 181;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 182;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 183;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 184;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 185;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 186;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 187;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 188;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 189;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 190;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 191;BA.debugLine="panelSintoma.Initialize(\"\")";
mostCurrent._panelsintoma.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 192;BA.debugLine="panelSintoma.LoadLayout(\"lay_Enfermedades_Sintoma";
mostCurrent._panelsintoma.LoadLayout("lay_Enfermedades_Sintomas",mostCurrent.activityBA);
 //BA.debugLineNum = 193;BA.debugLine="lblSintoma_titulo.Text = \"ERUPCIONES\"";
mostCurrent._lblsintoma_titulo.setText(BA.ObjectToCharSequence("ERUPCIONES"));
 //BA.debugLineNum = 194;BA.debugLine="lblSintoma_Descripcion.Text = \"En brazos y tronco";
mostCurrent._lblsintoma_descripcion.setText(BA.ObjectToCharSequence("En brazos y tronco"));
 //BA.debugLineNum = 195;BA.debugLine="imgSintoma.Bitmap = LoadBitmap(File.DirAssets, \"e";
mostCurrent._imgsintoma.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"enfermedades_erupciones.png").getObject()));
 //BA.debugLineNum = 196;BA.debugLine="Activity.AddView(panelSintoma, 10%x, 10%y, 80%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelsintoma.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _butfiebre_click() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub butFiebre_Click";
 //BA.debugLineNum = 140;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 141;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 148;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 149;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 150;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 152;BA.debugLine="panelSintoma.Initialize(\"\")";
mostCurrent._panelsintoma.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 153;BA.debugLine="panelSintoma.LoadLayout(\"lay_Enfermedades_Sintoma";
mostCurrent._panelsintoma.LoadLayout("lay_Enfermedades_Sintomas",mostCurrent.activityBA);
 //BA.debugLineNum = 154;BA.debugLine="lblSintoma_titulo.Text = \"FIEBRE\"";
mostCurrent._lblsintoma_titulo.setText(BA.ObjectToCharSequence("FIEBRE"));
 //BA.debugLineNum = 155;BA.debugLine="lblSintoma_Descripcion.Text = \"Puede ser alta\"";
mostCurrent._lblsintoma_descripcion.setText(BA.ObjectToCharSequence("Puede ser alta"));
 //BA.debugLineNum = 156;BA.debugLine="imgSintoma.Bitmap = LoadBitmap(File.DirAssets, \"e";
mostCurrent._imgsintoma.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"enfermedades_fiebre.png").getObject()));
 //BA.debugLineNum = 157;BA.debugLine="Activity.AddView(panelSintoma, 10%x, 10%y, 80%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelsintoma.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _butnauseas_click() throws Exception{
 //BA.debugLineNum = 160;BA.debugLine="Sub butNauseas_Click";
 //BA.debugLineNum = 161;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 162;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 164;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 165;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 167;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 168;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 169;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 170;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 171;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 172;BA.debugLine="panelSintoma.Initialize(\"\")";
mostCurrent._panelsintoma.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 173;BA.debugLine="panelSintoma.LoadLayout(\"lay_Enfermedades_Sintoma";
mostCurrent._panelsintoma.LoadLayout("lay_Enfermedades_Sintomas",mostCurrent.activityBA);
 //BA.debugLineNum = 174;BA.debugLine="lblSintoma_titulo.Text = \"NÁUSEAS\"";
mostCurrent._lblsintoma_titulo.setText(BA.ObjectToCharSequence("NÁUSEAS"));
 //BA.debugLineNum = 175;BA.debugLine="lblSintoma_Descripcion.Text = \"Vómitos / Pérdida";
mostCurrent._lblsintoma_descripcion.setText(BA.ObjectToCharSequence("Vómitos / Pérdida de apetito"));
 //BA.debugLineNum = 176;BA.debugLine="imgSintoma.Bitmap = LoadBitmap(File.DirAssets, \"e";
mostCurrent._imgsintoma.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"enfermedades_nauseas.png").getObject()));
 //BA.debugLineNum = 177;BA.debugLine="Activity.AddView(panelSintoma, 10%x, 10%y, 80%x,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelsintoma.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (80),mostCurrent.activityBA));
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _butzika_click() throws Exception{
 //BA.debugLineNum = 88;BA.debugLine="Sub butZika_Click";
 //BA.debugLineNum = 89;BA.debugLine="butFiebre.Visible = False";
mostCurrent._butfiebre.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 90;BA.debugLine="butNauseas.Visible = False";
mostCurrent._butnauseas.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 91;BA.debugLine="butZika.Visible = False";
mostCurrent._butzika.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 92;BA.debugLine="butErupciones.Visible = False";
mostCurrent._buterupciones.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 93;BA.debugLine="butDolor.Visible = False";
mostCurrent._butdolor.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="butChicungunya.Visible = False";
mostCurrent._butchicungunya.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 95;BA.debugLine="butDengue.Visible = False";
mostCurrent._butdengue.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 96;BA.debugLine="but_Cerrar.Visible = False";
mostCurrent._but_cerrar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 97;BA.debugLine="lblFondo.Initialize(\"\")";
mostCurrent._lblfondo.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 98;BA.debugLine="lblFondo.Color = Colors.ARGB(30,255,94,94)";
mostCurrent._lblfondo.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (30),(int) (255),(int) (94),(int) (94)));
 //BA.debugLineNum = 99;BA.debugLine="Activity.AddView(lblFondo,0,0,100%x,100%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblfondo.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 101;BA.debugLine="panelCriticos.Initialize(\"\")";
mostCurrent._panelcriticos.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 102;BA.debugLine="Activity.AddView(panelCriticos, 0,0, 100%x, 100%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._panelcriticos.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 103;BA.debugLine="panelCriticos.LoadLayout(\"lay_Enfermedades_Critic";
mostCurrent._panelcriticos.LoadLayout("lay_Enfermedades_Criticos",mostCurrent.activityBA);
 //BA.debugLineNum = 104;BA.debugLine="lblCriticos_texto.Text = \"En los casos graves (me";
mostCurrent._lblcriticos_texto.setText(BA.ObjectToCharSequence("En los casos graves (menos frecuentes), se lo asocia a complicaciones neurológicas, como el síndrome Guillán-Barré. A su vez, si la infección ocurre durante el embarazo, puede causar malformaciones y microcefalías en los recién nacidos"));
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private scrollExtraInfo As HorizontalScrollView";
mostCurrent._scrollextrainfo = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private imgHombre As ImageView";
mostCurrent._imghombre = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private butDolor As Button";
mostCurrent._butdolor = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private butErupciones As Button";
mostCurrent._buterupciones = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private butNauseas As Button";
mostCurrent._butnauseas = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private butFiebre As Button";
mostCurrent._butfiebre = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private lblTituloMain As Label";
mostCurrent._lbltitulomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private lblSubTituloMain As Label";
mostCurrent._lblsubtitulomain = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private butDengue As Button";
mostCurrent._butdengue = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private butZika As Button";
mostCurrent._butzika = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private butChicungunya As Button";
mostCurrent._butchicungunya = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lblSintoma_titulo As Label";
mostCurrent._lblsintoma_titulo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private imgSintoma As ImageView";
mostCurrent._imgsintoma = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lblSintoma_Descripcion As Label";
mostCurrent._lblsintoma_descripcion = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private btnCerrarSintoma As Button";
mostCurrent._btncerrarsintoma = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private panelSintoma As Panel";
mostCurrent._panelsintoma = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lblFondo As Label";
mostCurrent._lblfondo = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private lblCriticos_texto As Label";
mostCurrent._lblcriticos_texto = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private but_BackCriticos As Button";
mostCurrent._but_backcriticos = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private panelCriticos As Panel";
mostCurrent._panelcriticos = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private but_Cerrar As Button";
mostCurrent._but_cerrar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
