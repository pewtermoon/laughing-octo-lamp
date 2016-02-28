//Write sounds to file



// Dust randomly triggers Decay to create an exponential

// decay envelope for the WhiteNoise input source
(
{
//z = Decay.ar(Dust.ar(1,0.5), 0.3, WhiteNoise.ar);
z = SinOsc.ar(440);
//DelayL.ar(z, 0.2, 0.2, 1, z);
// input is mixed with delay via the add input
}.play
)

(
SynthDef("NRTsine",{
	arg freq, dur, deldur;
	var subosc, subenv, suboutput, suboscdel;
	subosc = {SinOsc.ar(freq)};
	suboscdel = DelayL.ar(subosc,2,deldur,1);
	suboscdel = subosc;
    subenv = {Line.ar(1, 0, dur, doneAction: 2)};
    suboutput = (suboscdel * subenv);
	Out.ar(0, suboutput)
}).writeDefFile;
)

//
(

SynthDef(\reverb12, {arg inbus=0, outbus=0, predelay=0.048, combdecay=5, allpassdecay=1, revVol=0.31;

	var sig, y, z;

	sig = In.ar(inbus, 2);

	z = DelayN.ar(sig, 0.1, predelay); // max 100 ms predelay

	y = Mix.ar(Array.fill(7,{ CombL.ar(z, 0.05, rrand(0.03, 0.05), combdecay) }));

	6.do({ y = AllpassN.ar(y, 0.050, rrand(0.03, 0.05), allpassdecay) });

	Out.ar(outbus, sig + (y * revVol));

}).add;

(
//Sine line
Pbind(\instrument, \NRTsine, \freq, Pseq([440, \rest],16),
	\dur, 0.4, \deldur, 4).play;
)

//Follow NRTsine, then replace in Score command
// the 'NRTsine' SynthDef
(
SynthDef("NRTsine",{
	arg freq, dur, deldur;
	var subosc, subenv, suboutput, suboscdel;
	subosc = {SinOsc.ar(freq)};
	suboscdel = DelayL.ar(subosc,deldur,deldur,1,subosc.value);
    subenv = {Line.ar(1, 0, dur, doneAction: 2)};
    suboutput = (suboscdel * subenv);
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
	\dur, 0.5,);
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
tune = Ppar([p],1);
samplePath = thisProcess.nowExecutingPath.dirname;
wavFile = samplePath +/+ "netherlands_first_day.wav";
tune.render(wavFile, 32.0, headerFormat: "WAV");
)

//This is all great but want to schedule events to occur at
//certain times

//Also want to add echo/delay into the SynthDef