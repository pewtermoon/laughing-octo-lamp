//Write sounds to file

//Follow NRTsine, then replace in Score command
// the 'NRTsine' SynthDef
(
SynthDef("NRTsine",{
	arg freq;
	var subosc, subenv, suboutput;
	subosc = {SinOsc.ar(freq)};
    subenv = {Line.ar(1, 0, 0.25, doneAction: 2)};
    suboutput = (subosc * subenv);
	Out.ar(0, suboutput)
}).writeDefFile;
)


//Kick drum
(
SynthDef('fullkickdrum', { arg dur = 0.25;
  var subosc, subenv, suboutput, clickosc, clickenv, clickoutput;

    subosc = {SinOsc.ar(60)};
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
SynthDef('openhat', {
	var hatosc, hatenv, hatnoise, hatoutput;
	hatnoise = {LPF.ar(WhiteNoise.ar(1),6000)};
	hatosc = {HPF.ar(hatnoise,2000)};
	hatenv = {Line.ar(1, 0, 0.3)};
	hatoutput = (hatosc * hatenv);

	Out.ar(0,
		Pan2.ar(hatoutput, 0)
	)
}).writeDefFile;
)

//Note: the Score syntax is v. awkward. Want to find a way to use
//my synth defs in a pattern then output to file.
//Pbind(\instrument, \fullkickdrum, \freq, Prand([1, 1.2, 2, 2.5, 3, 4], inf) * 200, \dur, 0.1).play;

//Render pattern with custom instrument
(
var samplePath, wavFile, p;
p = Pbind(
\instrument, \NRTsine,	\freq, Pseq([440, \rest ,220, \rest],10), \dur, 0.1,);
samplePath = thisProcess.nowExecutingPath.dirname;
wavFile = samplePath +/+ "christmas_tree.wav";
p.render(wavFile, 4.0, headerFormat: "WAV");
)
