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

public class frmcomotransmiten extends Activity implements B4AActivity{
	public static frmcomotransmiten mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "caza.mosquito", "caza.mosquito.frmcomotransmiten");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (frmcomotransmiten).");
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
		activityBA = new BA(this, layout, processBA, "caza.mosquito", "caza.mosquito.frmcomotransmiten");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.frmcomotransmiten", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (frmcomotransmiten) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (frmcomotransmiten) Resume **");
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
		return frmcomotransmiten.class;
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
            BA.LogInfo("** Activity (frmcomotransmiten) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (frmcomotransmiten) Pause event (activity is not paused). **");
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
            frmcomotransmiten mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (frmcomotransmiten) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _timer1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcirclebase = null;
public caza.mosquito.roundslider_custom _roundslider_custom1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpaso3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbllabelpaso6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldescripcionpaso = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquitoinfectado2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquitosano = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgmosquitoinfectado1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgpersonainfectada1 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgpersonasana = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgpersonainfectada2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnplaypause = null;
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
public caza.mosquito.frmcomovemos_app _frmcomovemos_app = null;
public caza.mosquito.frmcomovemos_enfermedades _frmcomovemos_enfermedades = null;
public caza.mosquito.frmcomovemos_porqueexiste _frmcomovemos_porqueexiste = null;
public caza.mosquito.frmdatosanteriores _frmdatosanteriores = null;
public caza.mosquito.frmdondecria _frmdondecria = null;
public caza.mosquito.frmeditprofile _frmeditprofile = null;
public caza.mosquito.frmelmosquito _frmelmosquito = null;
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
 //BA.debugLineNum = 41;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 43;BA.debugLine="Activity.LoadLayout(\"lay_ComoTransmiten_Main\")";
mostCurrent._activity.LoadLayout("lay_ComoTransmiten_Main",mostCurrent.activityBA);
 //BA.debugLineNum = 44;BA.debugLine="RoundSlider_Custom1.Value = 0";
mostCurrent._roundslider_custom1._setvalue /*int*/ ((int) (0));
 //BA.debugLineNum = 45;BA.debugLine="imgMosquitoSano.Visible = False";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 46;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 47;BA.debugLine="Timer1.Initialize(\"Timer1\", 100)";
_timer1.Initialize(processBA,"Timer1",(long) (100));
 //BA.debugLineNum = 48;BA.debugLine="Timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 49;BA.debugLine="lblPaso1.Visible = False";
mostCurrent._lblpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 50;BA.debugLine="lblPaso2.Visible = False";
mostCurrent._lblpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 51;BA.debugLine="lblPaso3.Visible = False";
mostCurrent._lblpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 52;BA.debugLine="lblDescripcionPaso.Text = \"Gira el círculo de col";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("Gira el círculo de colores para ver cómo se transmiten las enfermedades"));
 //BA.debugLineNum = 53;BA.debugLine="utilidades.ResetUserFontScale(Activity)";
mostCurrent._utilidades._resetuserfontscale /*String*/ (mostCurrent.activityBA,(anywheresoftware.b4a.objects.PanelWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.PanelWrapper(), (android.view.ViewGroup)(mostCurrent._activity.getObject())));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 70;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _btnplaypause_click() throws Exception{
 //BA.debugLineNum = 195;BA.debugLine="Sub btnPlayPause_Click";
 //BA.debugLineNum = 196;BA.debugLine="If btnPlayPause.Text = \"\" Then";
if ((mostCurrent._btnplaypause.getText()).equals("")) { 
 //BA.debugLineNum = 197;BA.debugLine="Timer1.Enabled = True";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 198;BA.debugLine="btnPlayPause.Text = \"\"";
mostCurrent._btnplaypause.setText(BA.ObjectToCharSequence(""));
 }else {
 //BA.debugLineNum = 200;BA.debugLine="Timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 201;BA.debugLine="btnPlayPause.Text = \"\"";
mostCurrent._btnplaypause.setText(BA.ObjectToCharSequence(""));
 };
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
return "";
}
public static String  _but_cerrar_click() throws Exception{
 //BA.debugLineNum = 206;BA.debugLine="Sub but_Cerrar_Click";
 //BA.debugLineNum = 207;BA.debugLine="Activity.RemoveAllViews";
mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 208;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 209;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private lblCircleBase As Label";
mostCurrent._lblcirclebase = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private RoundSlider_Custom1 As RoundSlider_Custom";
mostCurrent._roundslider_custom1 = new caza.mosquito.roundslider_custom();
 //BA.debugLineNum = 19;BA.debugLine="Private lblPaso1 As Label";
mostCurrent._lblpaso1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private lblPaso2 As Label";
mostCurrent._lblpaso2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private lblPaso3 As Label";
mostCurrent._lblpaso3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private lblLabelPaso1 As Label";
mostCurrent._lbllabelpaso1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private lblLabelPaso3 As Label";
mostCurrent._lbllabelpaso3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblLabelPaso2 As Label";
mostCurrent._lbllabelpaso2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private lblLabelPaso4 As Label";
mostCurrent._lbllabelpaso4 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblLabelPaso5 As Label";
mostCurrent._lbllabelpaso5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lblLabelPaso6 As Label";
mostCurrent._lbllabelpaso6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private lblDescripcionPaso As Label";
mostCurrent._lbldescripcionpaso = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private imgMosquitoInfectado2 As ImageView";
mostCurrent._imgmosquitoinfectado2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private imgMosquitoSano As ImageView";
mostCurrent._imgmosquitosano = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private imgMosquitoInfectado1 As ImageView";
mostCurrent._imgmosquitoinfectado1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private imgPersonaInfectada1 As ImageView";
mostCurrent._imgpersonainfectada1 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private imgPersonaSana As ImageView";
mostCurrent._imgpersonasana = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private imgPersonaInfectada2 As ImageView";
mostCurrent._imgpersonainfectada2 = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private btnPlayPause As Button";
mostCurrent._btnplaypause = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Dim Timer1 As Timer";
_timer1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 11;BA.debugLine="End Sub";
return "";
}
public static String  _roundslider_custom1_valuechanged(int _mvalue) throws Exception{
 //BA.debugLineNum = 74;BA.debugLine="Private Sub RoundSlider_Custom1_ValueChanged(mvalu";
 //BA.debugLineNum = 75;BA.debugLine="Timer1.Enabled = False";
_timer1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 76;BA.debugLine="RoundSlider_Update(mvalue)";
_roundslider_update(_mvalue);
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _roundslider_update(int _mvalue) throws Exception{
 //BA.debugLineNum = 78;BA.debugLine="Private Sub RoundSlider_Update(mvalue As Int)";
 //BA.debugLineNum = 79;BA.debugLine="If (mvalue >=0 And mvalue < 12.5) Then";
if ((_mvalue>=0 && _mvalue<12.5)) { 
 //BA.debugLineNum = 81;BA.debugLine="RoundSlider_Custom1.ThumbColor = Colors.ARGB(255";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (94),(int) (195),(int) (165));
 //BA.debugLineNum = 85;BA.debugLine="lblLabelPaso1.Visible = True";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 86;BA.debugLine="lblLabelPaso2.Visible = False";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="lblLabelPaso3.Visible = False";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 88;BA.debugLine="lblLabelPaso4.Visible = False";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 89;BA.debugLine="lblLabelPaso5.Visible = False";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 90;BA.debugLine="lblLabelPaso6.Visible = False";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 92;BA.debugLine="imgMosquitoSano.Visible = True";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 93;BA.debugLine="imgPersonaInfectada1.Visible = False";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="imgPersonaInfectada2.Visible = False";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 95;BA.debugLine="imgMosquitoInfectado1.Visible = False";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 96;BA.debugLine="imgMosquitoInfectado2.Visible = False";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 97;BA.debugLine="imgPersonaSana.Visible = False";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 98;BA.debugLine="lblDescripcionPaso.Text = \"Al nacer, los mosquit";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("Al nacer, los mosquitos no están infectados"));
 }else if((_mvalue>=12.5 && _mvalue<25)) { 
 //BA.debugLineNum = 100;BA.debugLine="RoundSlider_Custom1.ThumbColor = Colors.ARGB(255";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (241),(int) (65),(int) (77));
 //BA.debugLineNum = 104;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 105;BA.debugLine="lblLabelPaso2.Visible = True";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 106;BA.debugLine="lblLabelPaso3.Visible = False";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 107;BA.debugLine="lblLabelPaso4.Visible = False";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 108;BA.debugLine="lblLabelPaso5.Visible = False";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 109;BA.debugLine="lblLabelPaso6.Visible = False";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 111;BA.debugLine="imgMosquitoSano.Visible = True";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 112;BA.debugLine="imgPersonaInfectada1.Visible = True";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 113;BA.debugLine="imgPersonaInfectada2.Visible = False";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 114;BA.debugLine="imgMosquitoInfectado1.Visible = False";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 115;BA.debugLine="imgMosquitoInfectado2.Visible = False";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 116;BA.debugLine="imgPersonaSana.Visible = False";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 117;BA.debugLine="lblDescripcionPaso.Text = \"El mosquito sano pica";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("El mosquito sano pica a una persona infectada e incorpora el virus"));
 }else if((_mvalue>=25 && _mvalue<50)) { 
 //BA.debugLineNum = 119;BA.debugLine="RoundSlider_Custom1.ThumbColor = Colors.ARGB(255";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (241),(int) (65),(int) (77));
 //BA.debugLineNum = 123;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 124;BA.debugLine="lblLabelPaso2.Visible = False";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 125;BA.debugLine="lblLabelPaso3.Visible = True";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 126;BA.debugLine="lblLabelPaso4.Visible = False";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 127;BA.debugLine="lblLabelPaso5.Visible = False";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 128;BA.debugLine="lblLabelPaso6.Visible = False";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 129;BA.debugLine="imgMosquitoSano.Visible = False";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 130;BA.debugLine="imgPersonaInfectada1.Visible = False";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 131;BA.debugLine="imgPersonaInfectada2.Visible = False";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 132;BA.debugLine="imgMosquitoInfectado1.Visible = True";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 133;BA.debugLine="imgMosquitoInfectado2.Visible = False";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 134;BA.debugLine="imgPersonaSana.Visible = False";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 135;BA.debugLine="lblDescripcionPaso.Text = \"Luego de un tiempo de";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("Luego de un tiempo de incubación, el virus puede ser transmitido a otra persona, si lo pica un mosquito infectado."));
 }else if((_mvalue>=50 && _mvalue<62.5)) { 
 //BA.debugLineNum = 137;BA.debugLine="RoundSlider_Custom1.ThumbColor = Colors.ARGB(255";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (94),(int) (195),(int) (165));
 //BA.debugLineNum = 141;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 142;BA.debugLine="lblLabelPaso2.Visible = False";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="lblLabelPaso3.Visible = False";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="lblLabelPaso4.Visible = True";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 145;BA.debugLine="lblLabelPaso5.Visible = False";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="lblLabelPaso6.Visible = False";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="imgMosquitoSano.Visible = False";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 148;BA.debugLine="imgPersonaInfectada1.Visible = False";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 149;BA.debugLine="imgPersonaInfectada2.Visible = False";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 150;BA.debugLine="imgMosquitoInfectado1.Visible = True";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 151;BA.debugLine="imgMosquitoInfectado2.Visible = False";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 152;BA.debugLine="imgPersonaSana.Visible = True";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 153;BA.debugLine="lblDescripcionPaso.Text = \"El mosquito infectado";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("El mosquito infectado puede seguir transmitiendo el virus a otras personas, y la persona infectada servir como fuente de virus para otros mosquitos"));
 }else if((_mvalue>=62.5 && _mvalue<75)) { 
 //BA.debugLineNum = 155;BA.debugLine="RoundSlider_Custom1.ThumbColor =  Colors.ARGB(25";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (241),(int) (65),(int) (77));
 //BA.debugLineNum = 159;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 160;BA.debugLine="lblLabelPaso2.Visible = False";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="lblLabelPaso3.Visible = False";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 162;BA.debugLine="lblLabelPaso4.Visible = False";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 163;BA.debugLine="lblLabelPaso5.Visible = True";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 164;BA.debugLine="lblLabelPaso6.Visible = True";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 165;BA.debugLine="imgMosquitoSano.Visible = False";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 166;BA.debugLine="imgPersonaInfectada1.Visible = False";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 167;BA.debugLine="imgPersonaInfectada2.Visible = True";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 168;BA.debugLine="imgMosquitoInfectado1.Visible = False";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 169;BA.debugLine="imgMosquitoInfectado2.Visible = True";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 170;BA.debugLine="imgPersonaSana.Visible = False";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="lblDescripcionPaso.Text = \"El mosquito infectado";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("El mosquito infectado puede seguir transmitiendo el virus a otras personas, y la persona infectada servir como fuente de virus para otros mosquitos"));
 }else if((_mvalue>=87.5)) { 
 //BA.debugLineNum = 174;BA.debugLine="RoundSlider_Custom1.ThumbColor = Colors.ARGB(255";
mostCurrent._roundslider_custom1._thumbcolor /*int*/  = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (94),(int) (195),(int) (165));
 //BA.debugLineNum = 178;BA.debugLine="lblLabelPaso1.Visible = False";
mostCurrent._lbllabelpaso1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 179;BA.debugLine="lblLabelPaso2.Visible = False";
mostCurrent._lbllabelpaso2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 180;BA.debugLine="lblLabelPaso3.Visible = False";
mostCurrent._lbllabelpaso3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 181;BA.debugLine="lblLabelPaso4.Visible = False";
mostCurrent._lbllabelpaso4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 182;BA.debugLine="lblLabelPaso5.Visible = False";
mostCurrent._lbllabelpaso5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 183;BA.debugLine="lblLabelPaso6.Visible = False";
mostCurrent._lbllabelpaso6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 184;BA.debugLine="imgMosquitoSano.Visible = True";
mostCurrent._imgmosquitosano.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 185;BA.debugLine="imgPersonaInfectada1.Visible = False";
mostCurrent._imgpersonainfectada1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 186;BA.debugLine="imgPersonaInfectada2.Visible = False";
mostCurrent._imgpersonainfectada2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 187;BA.debugLine="imgMosquitoInfectado1.Visible = False";
mostCurrent._imgmosquitoinfectado1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 188;BA.debugLine="imgMosquitoInfectado2.Visible = False";
mostCurrent._imgmosquitoinfectado2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 189;BA.debugLine="imgPersonaSana.Visible = False";
mostCurrent._imgpersonasana.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 190;BA.debugLine="lblDescripcionPaso.Text = \"El ciclo vuelve a emp";
mostCurrent._lbldescripcionpaso.setText(BA.ObjectToCharSequence("El ciclo vuelve a empezar"+anywheresoftware.b4a.keywords.Common.CRLF+"con un mosquito sano distinto."));
 };
 //BA.debugLineNum = 192;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Timer1_Tick";
 //BA.debugLineNum = 57;BA.debugLine="If RoundSlider_Custom1.Value < 100 Then";
if (mostCurrent._roundslider_custom1._getvalue /*int*/ ()<100) { 
 //BA.debugLineNum = 58;BA.debugLine="RoundSlider_Custom1.Value = RoundSlider_Custom1.";
mostCurrent._roundslider_custom1._setvalue /*int*/ ((int) (mostCurrent._roundslider_custom1._getvalue /*int*/ ()+1));
 //BA.debugLineNum = 59;BA.debugLine="RoundSlider_Update(RoundSlider_Custom1.Value)";
_roundslider_update(mostCurrent._roundslider_custom1._getvalue /*int*/ ());
 }else {
 //BA.debugLineNum = 61;BA.debugLine="RoundSlider_Custom1.Value = 0";
mostCurrent._roundslider_custom1._setvalue /*int*/ ((int) (0));
 //BA.debugLineNum = 62;BA.debugLine="RoundSlider_Update(RoundSlider_Custom1.Value)";
_roundslider_update(mostCurrent._roundslider_custom1._getvalue /*int*/ ());
 };
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
}
