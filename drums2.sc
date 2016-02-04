//Write sounds to file

//Follow NRTsine, then replace in Score command
// the 'NRTsine' SynthDef
(
SynthDef("NRTsine",{
	arg freq, dur;
	var subosc, subenv, suboutput;
	subosc = {SinOsc.ar(freq)};
    subenv = {Line.ar(0.9, 0, dur, doneAction: 2)};
    suboutput = (subosc * subenv);
	Out.ar(0, suboutput)
}).writeDefFile;
)


//Kick drum
(
SynthDef('fullkickdrum', { arg dur, freq;
  var subosc, subenv, suboutput, clickosc, clickenv, clickoutput;

    subosc = {SinOsc.ar(freq)};
    subenv = {Line.ar(1, 0, dur, doneAction: 2)};

    clickosc = {LPF.ar(WhiteNoise.ar(1),1500)};
    clickenv = {Line.ar(1, 0, 0.02)};

    suboutput = (subosc * subenv);
    clickoutput = (clickosc * clickenv);

    Out.ar(0,
        Pan2.ar(suboutput + clickoutput, 0)
    )
}).writeDefFile;
)

(
SynthDef('openhat', {arg freq, dur;
	var hatosc, hatenv, hatnoise, hatoutput;
	hatnoise = {LPF.ar(WhiteNoise.ar(freq),6000)};
	hatosc = {HPF.ar(hatnoise,2000)};
	hatenv = {Line.ar(1, 0, dur)};
	hatoutput = (hatosc * hatenv);

	Out.ar(0,
		Pan2.ar(hatoutput, 0)
	)
}).writeDefFile;
)

//Render pattern with custom instruments
(
var samplePath, wavFile, p, x, kickline, tune, kfrq, hihatline,
hhfrq;
kfrq = 60;
hhfrq = 1;
//Sine line
x = Pxrand([1,5/4,3/2,15/8, \rest],32)*440;
p = Pbind(
\instrument, \NRTsine,	\freq,
	Pxrand([x,x*5/4,x*3/2,x*15/8],inf),
	\dur, 0.125,);
//Kickline
kickline = Pbind(\instrument, \fullkickdrum, \freq,
Pseq([kfrq, \rest, kfrq, \rest,
		kfrq, kfrq, \rest, kfrq],inf), \dur, 0.125);
//Hihat line
hihatline = Pbind(\instrument, \openhat, \freq,
	Pxrand([hhfrq,hhfrq,\rest],inf),
//	Pseq([hhfrq,hhfrq,\rest,\rest,
//		hhfrq,\rest,hhfrq,\rest],inf),
	\dur, 0.125);
//Whole thing
tune = Ppar([p, kickline, hihatline],1);
samplePath = thisProcess.nowExecutingPath.dirname;
wavFile = samplePath +/+ "netherlands_first_day.wav";
tune.render(wavFile, 32.0, headerFormat: "WAV");
)
