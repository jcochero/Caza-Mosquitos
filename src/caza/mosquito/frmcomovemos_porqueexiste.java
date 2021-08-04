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

public class frmcomovemos_porqueexiste extends Activity implements B4AActivity{
	public static frmcomovemos_porqueexiste mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmcomovemos_porqueexiste");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmcomovemos_porqueexiste).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmcomovemos_porqueexiste");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmcomovemos_porqueexiste", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmcomovemos_porqueexiste) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmcomovemos_porqueexiste) Resume **");
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
		return frmcomovemos_porqueexiste.class;
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
            BA.LogInfo("** Activity (frmcomovemos_porqueexiste) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmcomovemos_porqueexiste) Pause event (activity is not paused). **");
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
            frmcomovemos_porqueexiste mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmcomovemos_porqueexiste) Resume **");
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
public anywheresoftware.b4a.objects.HorizontalScrollViewWrapper _slide_comovemos_paneles = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _lbliconocentral_ico = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbliconocentral_text = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltextoporque = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblarrow = null;
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
 //BA.debugLineNum = 25;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 27;BA.debugLine="Activity.LoadLayout(\"lay_ComoVemos_Porque\")";
mostCurrent._activity.LoadLayout("lay_ComoVemos_Porque",mostCurrent.activityBA);
 //BA.debugLineNum = 28;BA.debugLine="slide_ComoVemos_Paneles.Panel.LoadLayout(\"lay_Com";
mostCurrent._slide_comovemos_paneles.getPanel().LoadLayout("lay_ComoVemos_Porque_Paneles",mostCurrent.activityBA);
 //BA.debugLineNum = 29;BA.debugLine="slide_ComoVemos_Paneles.Panel.Width = 1200dip";
mostCurrent._slide_comovemos_paneles.getPanel().setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1200)));
 //BA.debugLineNum = 30;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._vvvvvvvvvvvvvvvvvvvvv7._vvvvvvvvv0 /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 37;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono1_click() throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub btnIcono1_Click";
 //BA.debugLineNum = 48;BA.debugLine="lblTextoPorque.Text = \" Como toda problemática de";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence(" Como toda problemática de salud, se vincula a como funcionamos y nos organizamos como sociedad. Cómo nos movemos, nos vinculamos,  manejamos nuestros residuos, el agua y cómo decidimos y buscamos resolver nuestros problemas."));
 //BA.debugLineNum = 49;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 50;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 51;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 52;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono2_click() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Sub btnIcono2_Click";
 //BA.debugLineNum = 59;BA.debugLine="lblTextoPorque.Text = \"Como toda problemática soc";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("Como toda problemática social, tiene una historia. Por ejemplo, ¿sabían que los mosquitos Aedes aegypti llegaron a América en barcos en los que transportaban esclavos durante la época de la conquista?"));
 //BA.debugLineNum = 60;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 61;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 62;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 63;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 68;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono3_click() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Sub btnIcono3_Click";
 //BA.debugLineNum = 70;BA.debugLine="lblTextoPorque.Text = \"¿Ocurre de la misma manera";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("¿Ocurre de la misma manera la problemática en diferentes culturas? La forma en la que habitamos nuestros territorios, nuestras costumbres pueden favorecer o no la convivencia con el mosquito y las enfermedades."));
 //BA.debugLineNum = 71;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 72;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 73;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono4_click() throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub btnIcono4_Click";
 //BA.debugLineNum = 81;BA.debugLine="lblTextoPorque.Text = \"Las características especi";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("Las características especificas del mosquito vector y de los virus son también claves para entender la problemática."));
 //BA.debugLineNum = 82;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 83;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 84;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 85;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono5_click() throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub btnIcono5_Click";
 //BA.debugLineNum = 92;BA.debugLine="lblTextoPorque.Text = \"Las características propia";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("Las características propias de las enfermedades, sus síntomas y formas de tratamiento también son una pieza fundamental del rompecabezas."));
 //BA.debugLineNum = 93;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 94;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 95;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 96;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono6_click() throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Sub btnIcono6_Click";
 //BA.debugLineNum = 103;BA.debugLine="lblTextoPorque.Text = \"¿Cómo afectan las decision";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("¿Cómo afectan las decisiones políticas a la problemática? El acceso a la salud, al agua, la forma en la que se manejan los residuos y la toma de decisiones son temas políticos vinculados a la problemática."));
 //BA.debugLineNum = 104;BA.debugLine="lblTextoPorque.Padding = Array As Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 105;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 106;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 107;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 112;BA.debugLine="End Sub";
return "";
}
public static String  _btnicono7_click() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub btnIcono7_Click";
 //BA.debugLineNum = 114;BA.debugLine="lblTextoPorque.Text = \"¿Existen intereses económi";
mostCurrent._lbltextoporque.setText(BA.ObjectToCharSequence("¿Existen intereses económicos que atraviesan las problemáticas de salud? Las inversiones (o ausencia de ellas) en infraestructura, los vínculos con sectores privados y la venta de tratamientos o repelentes, entre otras cosas, también son parte del asunto"));
 //BA.debugLineNum = 115;BA.debugLine="lblTextoPorque.Padding = Array as Int(10dip, 10di";
mostCurrent._lbltextoporque.setPadding(new int[]{anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10))});
 //BA.debugLineNum = 116;BA.debugLine="If lblTextoPorque.Visible = False Then";
if (mostCurrent._lbltextoporque.getVisible()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 117;BA.debugLine="lblTextoPorque.Visible = True";
mostCurrent._lbltextoporque.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="lblArrow.Visible = True";
mostCurrent._lblarrow.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _but_backporque_click() throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub but_BackPorque_Click";
 //BA.debugLineNum = 43;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 44;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private slide_ComoVemos_Paneles As HorizontalScro";
mostCurrent._slide_comovemos_paneles = new anywheresoftware.b4a.objects.HorizontalScrollViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private lblIconoCentral_ico As ImageView";
mostCurrent._lbliconocentral_ico = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private lblIconoCentral_text As Label";
mostCurrent._lbliconocentral_text = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private lblTextoPorque As Label";
mostCurrent._lbltextoporque = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private lblArrow As Label";
mostCurrent._lblarrow = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
