package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "caza.mosquito", "caza.mosquito.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static String _v7 = "";
public static anywheresoftware.b4a.sql.SQL _v0 = null;
public static anywheresoftware.b4a.sql.SQL _vv1 = null;
public static String _vv2 = "";
public static String _vv3 = "";
public static anywheresoftware.b4a.objects.FirebaseAuthWrapper _vv4 = null;
public static anywheresoftware.b4x.objects.FacebookSdkWrapper _vv5 = null;
public static anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper _vv6 = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
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
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 41;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 42;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return false;
}
public static String  _auth_signedin(anywheresoftware.b4a.objects.FirebaseAuthWrapper.FirebaseUserWrapper _user) throws Exception{
 //BA.debugLineNum = 49;BA.debugLine="Sub Auth_SignedIn (User As FirebaseUser)";
 //BA.debugLineNum = 53;BA.debugLine="Log(\"signed in with google!\")";
anywheresoftware.b4a.keywords.Common.LogImpl("53473412","signed in with google!",0);
 //BA.debugLineNum = 54;BA.debugLine="Main.username = User.Email";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv7 /*String*/  = _user.getEmail();
 //BA.debugLineNum = 55;BA.debugLine="Main.strUserID = User.Email";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvv7 /*String*/  = _user.getEmail();
 //BA.debugLineNum = 56;BA.debugLine="Main.strUserFullName = User.DisplayName";
mostCurrent._vvvvvvvvvvvvvvvvvv2._vvvvvvvvvvvv3 /*String*/  = _user.getDisplayName();
 //BA.debugLineNum = 57;BA.debugLine="CallSubDelayed(frmLogin, \"LoginOk_Firebase\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvv6.getObject()),"LoginOk_Firebase");
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim savedir As String";
_v7 = "";
 //BA.debugLineNum = 10;BA.debugLine="Dim sqlDB As SQL";
_v0 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 11;BA.debugLine="Dim speciesDB As SQL";
_vv1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 12;BA.debugLine="Dim speciesDBDir As String";
_vv2 = "";
 //BA.debugLineNum = 13;BA.debugLine="Dim dbdir As String";
_vv3 = "";
 //BA.debugLineNum = 15;BA.debugLine="Public auth As FirebaseAuth";
_vv4 = new anywheresoftware.b4a.objects.FirebaseAuthWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Public facebook As FacebookSdk";
_vv5 = new anywheresoftware.b4x.objects.FacebookSdkWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim fm As FirebaseMessaging";
_vv6 = new anywheresoftware.b4a.objects.FirebaseNotificationsService.FirebaseMessageWrapper();
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 20;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 22;BA.debugLine="CallSubDelayed(FirebaseMessaging, \"SubscribeToTop";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(mostCurrent._vvvvvvvvvvvvvvvvvvv1.getObject()),"SubscribeToTopics");
 //BA.debugLineNum = 23;BA.debugLine="auth.Initialize(\"auth\")";
_vv4.Initialize(processBA,"auth");
 //BA.debugLineNum = 24;BA.debugLine="auth.SignOutFromGoogle";
_vv4.SignOutFromGoogle();
 //BA.debugLineNum = 25;BA.debugLine="facebook.Initialize";
_vv5.Initialize();
 //BA.debugLineNum = 27;BA.debugLine="fm.Initialize(\"fm\")";
_vv6.Initialize(processBA,"fm");
 //BA.debugLineNum = 29;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 45;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 47;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 31;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 36;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _vv7() throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Public Sub UpdateFCMToken";
 //BA.debugLineNum = 64;BA.debugLine="fm.SubscribeToTopic(\"general\") 'you can subscribe";
_vv6.SubscribeToTopic("general");
 //BA.debugLineNum = 65;BA.debugLine="Log (\"NewToken: \" & fm.Token)";
anywheresoftware.b4a.keywords.Common.LogImpl("53538947","NewToken: "+_vv6.getToken(),0);
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
}
