package caza.mosquito;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class downloadservice extends  android.app.Service{
	public static class downloadservice_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (downloadservice) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, downloadservice.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, false, BA.class);
		}

	}
    static downloadservice mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return downloadservice.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "caza.mosquito", "caza.mosquito.downloadservice");
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
			processBA.raiseEvent2(null, true, "CREATE", true, "caza.mosquito.downloadservice", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!false && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (downloadservice) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (false) {
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
                    BA.LogInfo("** Service (downloadservice) Create **");
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
        if (false)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (downloadservice) Start **");
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
        if (false) {
            BA.LogInfo("** Service (downloadservice) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (downloadservice) Destroy **");
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
public static anywheresoftware.b4a.objects.collections.Map _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = null;
public static anywheresoftware.b4a.objects.Timer _vvvvvvv1 = null;
public static anywheresoftware.b4a.phone.Phone.PhoneWakeState _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.main _vvvvvvvvvvvvvvvvvv2 = null;
public caza.mosquito.frmprincipal _vvvvvvvvvvvvvvvvvv3 = null;
public caza.mosquito.starter _vvvvvvvvvvvvvvvvvv4 = null;
public caza.mosquito.frmcomofotos _vvvvvvvvvvvvvvvvvv5 = null;
public caza.mosquito.frmlogin _vvvvvvvvvvvvvvvvvv6 = null;
public caza.mosquito.dbutils _vvvvvvvvvvvvvvvvvv7 = null;
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
public static class _downloaddata{
public boolean IsInitialized;
public String url;
public Object Target;
public String EventName;
public void Initialize() {
IsInitialized = true;
url = "";
Target = new Object();
EventName = "";
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static class _jobtag{
public boolean IsInitialized;
public caza.mosquito.downloadservice._downloaddata Data;
public anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput CountingStream;
public long Total;
public void Initialize() {
IsInitialized = true;
Data = new caza.mosquito.downloadservice._downloaddata();
CountingStream = new anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput();
Total = 0L;
}
@Override
		public String toString() {
			return BA.TypeToString(this, false);
		}}
public static String  _canceldownload(String _url) throws Exception{
caza.mosquito.httpjob _job = null;
caza.mosquito.downloadservice._jobtag _jt = null;
 //BA.debugLineNum = 61;BA.debugLine="Public Sub CancelDownload(url As String)";
 //BA.debugLineNum = 62;BA.debugLine="If jobs.ContainsKey(url) = False Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.ContainsKey((Object)(_url))==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 63;BA.debugLine="Log(\"Ignoring cancel request.\")";
anywheresoftware.b4a.keywords.Common.LogImpl("511599874","Ignoring cancel request.",0);
 //BA.debugLineNum = 64;BA.debugLine="Log(\"url: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("511599875","url: "+_url,0);
 //BA.debugLineNum = 65;BA.debugLine="Log(jobs.Get(\"jobname\"))";
anywheresoftware.b4a.keywords.Common.LogImpl("511599876",BA.ObjectToString(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Get((Object)("jobname"))),0);
 //BA.debugLineNum = 66;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 68;BA.debugLine="Log(\"Cancelando: \" & url)";
anywheresoftware.b4a.keywords.Common.LogImpl("511599879","Cancelando: "+_url,0);
 //BA.debugLineNum = 69;BA.debugLine="Dim job As HttpJob = jobs.Get(url)";
_job = (caza.mosquito.httpjob)(_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Get((Object)(_url)));
 //BA.debugLineNum = 70;BA.debugLine="Dim jt As JobTag = job.Tag";
_jt = (caza.mosquito.downloadservice._jobtag)(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*Object*/ );
 //BA.debugLineNum = 71;BA.debugLine="If jt.CountingStream.IsInitialized Then";
if (_jt.CountingStream /*anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput*/ .IsInitialized()) { 
 //BA.debugLineNum = 72;BA.debugLine="jt.CountingStream.Close";
_jt.CountingStream /*anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput*/ .Close();
 }else {
 //BA.debugLineNum = 74;BA.debugLine="jt.Data.url = \"\"";
_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .url /*String*/  = "";
 };
 //BA.debugLineNum = 76;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Private Sub EndTimer";
 //BA.debugLineNum = 40;BA.debugLine="Service.StopForeground(1)";
mostCurrent._service.StopForeground((int) (1));
 //BA.debugLineNum = 41;BA.debugLine="timer1.Enabled = False";
_vvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 42;BA.debugLine="pw.ReleasePartialLock";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.ReleasePartialLock();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _jobdone(caza.mosquito.httpjob _job) throws Exception{
caza.mosquito.downloadservice._jobtag _jt = null;
 //BA.debugLineNum = 88;BA.debugLine="Sub JobDone(job As HttpJob)";
 //BA.debugLineNum = 89;BA.debugLine="jobs.Remove(job.JobName)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Remove((Object)(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 /*String*/ ));
 //BA.debugLineNum = 90;BA.debugLine="Dim jt As JobTag = job.Tag";
_jt = (caza.mosquito.downloadservice._jobtag)(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*Object*/ );
 //BA.debugLineNum = 91;BA.debugLine="If jobs.Size = 0 Then EndTimer";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.getSize()==0) { 
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv2();};
 //BA.debugLineNum = 92;BA.debugLine="If job.Success Then";
if (_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4 /*boolean*/ ) { 
 //BA.debugLineNum = 93;BA.debugLine="CallSubDelayed3(jt.Data.Target, jt.Data.EventNam";
anywheresoftware.b4a.keywords.Common.CallSubDelayed3(processBA,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .Target /*Object*/ ,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .EventName /*String*/ +"_Progress",(Object)(_jt.CountingStream /*anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput*/ .getCount()),(Object)(_jt.Total /*long*/ ));
 //BA.debugLineNum = 95;BA.debugLine="CallSubDelayed2(jt.Data.Target, jt.Data.EventNam";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .Target /*Object*/ ,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .EventName /*String*/ +"_Complete",(Object)(_job));
 }else {
 //BA.debugLineNum = 98;BA.debugLine="Log(job.ErrorMessage)";
anywheresoftware.b4a.keywords.Common.LogImpl("511730954",_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv6 /*String*/ ,0);
 //BA.debugLineNum = 99;BA.debugLine="CallSubDelayed2(jt.Data.Target, jt.Data.EventNam";
anywheresoftware.b4a.keywords.Common.CallSubDelayed2(processBA,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .Target /*Object*/ ,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .EventName /*String*/ +"_Complete",(Object)(_job));
 };
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 5;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 6;BA.debugLine="Private jobs As Map";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 7;BA.debugLine="Private timer1 As Timer";
_vvvvvvv1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 8;BA.debugLine="Type DownloadData (url As String, Target As Objec";
;
 //BA.debugLineNum = 9;BA.debugLine="Type JobTag (Data As DownloadData,  _ 		CountingS";
;
 //BA.debugLineNum = 11;BA.debugLine="Private pw As PhoneWakeState";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3 = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 13;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 14;BA.debugLine="jobs.Initialize";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Initialize();
 //BA.debugLineNum = 15;BA.debugLine="timer1.Initialize(\"timer1\", 1000)";
_vvvvvvv1.Initialize(processBA,"timer1",(long) (1000));
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 22;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 20;BA.debugLine="End Sub";
return "";
}
public static String  _startdownload(caza.mosquito.downloadservice._downloaddata _data) throws Exception{
caza.mosquito.httpjob _j = null;
caza.mosquito.downloadservice._jobtag _tag = null;
 //BA.debugLineNum = 45;BA.debugLine="Public Sub StartDownload(data As DownloadData)";
 //BA.debugLineNum = 46;BA.debugLine="If jobs.ContainsKey(data.url) Then";
if (_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.ContainsKey((Object)(_data.url /*String*/ ))) { 
 //BA.debugLineNum = 47;BA.debugLine="Log(\"Ignoring duplicate request.\")";
anywheresoftware.b4a.keywords.Common.LogImpl("511534338","Ignoring duplicate request.",0);
 //BA.debugLineNum = 48;BA.debugLine="Return";
if (true) return "";
 };
 //BA.debugLineNum = 50;BA.debugLine="Dim J As HttpJob";
_j = new caza.mosquito.httpjob();
 //BA.debugLineNum = 51;BA.debugLine="J.Initialize(data.url, Me)";
_j._initialize /*String*/ (processBA,_data.url /*String*/ ,downloadservice.getObject());
 //BA.debugLineNum = 52;BA.debugLine="Dim tag As JobTag";
_tag = new caza.mosquito.downloadservice._jobtag();
 //BA.debugLineNum = 53;BA.debugLine="tag.Initialize";
_tag.Initialize();
 //BA.debugLineNum = 54;BA.debugLine="tag.data = data";
_tag.Data /*caza.mosquito.downloadservice._downloaddata*/  = _data;
 //BA.debugLineNum = 55;BA.debugLine="J.tag = tag";
_j._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*Object*/  = (Object)(_tag);
 //BA.debugLineNum = 56;BA.debugLine="jobs.Put(data.url, J)";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Put((Object)(_data.url /*String*/ ),(Object)(_j));
 //BA.debugLineNum = 57;BA.debugLine="J.Download(data.url)";
_j._vvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*String*/ (_data.url /*String*/ );
 //BA.debugLineNum = 58;BA.debugLine="If timer1.Enabled = False Then StartTimer(data.Ta";
if (_vvvvvvv1.getEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4(_data.Target /*Object*/ );};
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv4(Object _target) throws Exception{
anywheresoftware.b4a.objects.NotificationWrapper _n = null;
 //BA.debugLineNum = 26;BA.debugLine="Private Sub StartTimer (Target As Object)";
 //BA.debugLineNum = 27;BA.debugLine="Dim n As Notification";
_n = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 28;BA.debugLine="n.Initialize";
_n.Initialize();
 //BA.debugLineNum = 29;BA.debugLine="n.Icon = \"icon\"";
_n.setIcon("icon");
 //BA.debugLineNum = 30;BA.debugLine="n.Vibrate = False";
_n.setVibrate(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 31;BA.debugLine="n.Sound = False";
_n.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 32;BA.debugLine="n.Light = False";
_n.setLight(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 33;BA.debugLine="n.SetInfo(\"Conectando con el servidor...\", \"\", Ta";
_n.SetInfoNew(processBA,BA.ObjectToCharSequence("Conectando con el servidor..."),BA.ObjectToCharSequence(""),_target);
 //BA.debugLineNum = 34;BA.debugLine="Service.StartForeground(1, n)";
mostCurrent._service.StartForeground((int) (1),(android.app.Notification)(_n.getObject()));
 //BA.debugLineNum = 35;BA.debugLine="timer1.Enabled = True";
_vvvvvvv1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 36;BA.debugLine="pw.PartialLock";
_vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv3.PartialLock(processBA);
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _timer1_tick() throws Exception{
caza.mosquito.httpjob _job = null;
caza.mosquito.downloadservice._jobtag _jt = null;
 //BA.debugLineNum = 78;BA.debugLine="Sub timer1_tick";
 //BA.debugLineNum = 79;BA.debugLine="For Each job As HttpJob In jobs.Values";
{
final anywheresoftware.b4a.BA.IterableList group1 = _vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1.Values();
final int groupLen1 = group1.getSize()
;int index1 = 0;
;
for (; index1 < groupLen1;index1++){
_job = (caza.mosquito.httpjob)(group1.Get(index1));
 //BA.debugLineNum = 80;BA.debugLine="Dim jt As JobTag = job.Tag";
_jt = (caza.mosquito.downloadservice._jobtag)(_job._vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv1 /*Object*/ );
 //BA.debugLineNum = 81;BA.debugLine="If jt.CountingStream.IsInitialized Then";
if (_jt.CountingStream /*anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput*/ .IsInitialized()) { 
 //BA.debugLineNum = 82;BA.debugLine="CallSub3(jt.Data.Target, jt.Data.EventName & \"_";
anywheresoftware.b4a.keywords.Common.CallSubNew3(processBA,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .Target /*Object*/ ,_jt.Data /*caza.mosquito.downloadservice._downloaddata*/ .EventName /*String*/ +"_Progress",(Object)(_jt.CountingStream /*anywheresoftware.b4a.randomaccessfile.CountingStreams.CountingOutput*/ .getCount()),(Object)(_jt.Total /*long*/ ));
 };
 }
};
 //BA.debugLineNum = 86;BA.debugLine="End Sub";
return "";
}
}
