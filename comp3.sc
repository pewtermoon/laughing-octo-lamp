//Note: if you do s.boot, then even after s.quit, you won't be able to hear
//or play ANY soundfile in ANY application outside SC. To stop this, run
//killall jackd in a terminal (this will force switching back from jackd to
//pulseaudio (Ubuntu's sound handling program).
var phrases, p1, p1x, p2, p2x, bass1, bass2, p3, p3x, rest4b, pl1, pl2, pl3, pp;

//Synth def
SynthDef(\syn1, { |freq = 440, amp = 1, scal = 0.5|
    var sig, env;
	sig = SinOsc.ar([freq-15, freq+15], 0, 0.3);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.25]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

SynthDef(\syn2, { |freq = 440, amp = 0.01, scal = 0.25, pan = 0.0|
    var sig, env;
	sig = Pulse.ar(XLine.kr(freq-50, freq-100, 0.125), 0.5, 0.15);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.5]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0,Pan2.ar( sig*env,pan));
}).writeDefFile;

//Pattern lib
~phrases = (
	p1: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1, \rest, 1.5, 2,
		             1.9, 1.8, 1.7, 1.6]*391.995, 1),
		\dur, 0.25,
		// \amp, 0.01,
		\scal, 0.5
	),

	p1x: Pbind(
		\instrument, \syn1,
		\freq, Pseq([2.9, 1.9, 1.9, 1.9,
		             2.2, 2, 2.2, 1.8]*587.330, 1),
		\dur, 0.25,
		\scal, 0.3
		// \amp, 0.01
	),

	p2: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8]*391.995, 1),
		\dur, 0.125,
		\scal, 0.1
		// \amp, 0.01
	),

		p2x: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8]*587.330*2, 1),
		\dur, 0.125,
		\scal, 0.1
		// \amp, 0.01
	),

	bass1: Pbind(
	\instrument, \syn2,
	\freq, Pseq([1, 0.8, 0.5, 0.4,
			     0.3, 0.6, 0.3, 0.6]*391.995),
	\dur, 0.25,
		// \amp, 0.01
	\pan, -0.8
	),

	bass2: Pbind(
	\instrument, \syn2,
	\freq, Pseq([0.7, 0.8, 0.9, 1,
			1.2, 0.6, 1, 1.2]*587.330),
	\dur, 0.25,
		// \amp, 0.01
	\pan, 0.8
	),

	p3: Pbind(
	\instrument, \syn1,
		\freq, Pseq([1, 0.9, 1.1, 0.8,
			              1.2, 0.7, 1.3, 0.6]*587.330*3,1),
	\dur, 0.25,
		// \amp, 0.01
	),

		p3x: Pbind(
	\instrument, \syn1,
		\freq, Pseq([1, 0.9, 1.1, 0.8,
			              1.2, 0.7, 1.3, 0.6]*391.995*3,1),
	\dur, 0.25,
		// \amp, 0.01
	),

	rest4b: Pbind(
	\instrument, \syn1,
	\freq, Pseq([\rest, \rest, \rest, \rest], 1),
		// \amp, 0
	)
);
//Set tempo
TempoClock.default.tempo = 1.2;

// the higher level control pattern is really simple now
// var pl1, pl2, pl3, pp, o, samplePath, wavFile, oscFile;
pl1 = Psym(Pseq(#[
	rest4b,
	rest4b,
	rest4b,
	rest4b,
	//
	// p1, \rest, \rest,
	// p1x, \rest, \rest,
	// p1, p1x,
	// p1, p1x,

	p1, \rest, \rest,
	p1x, \rest, \rest,
	p1, p1x,
	p1, \rest, \rest,

	p3, p3,
	rest4b,
	p3, p1x,
	p3, p2x,
	p3, p3x

], 1), ~phrases);
pl2 = Psym(Pseq(#[
	// rest4b,
	// rest4b,
	// rest4b,
	// rest4b,
	//
	rest4b,
	rest4b,
	rest4b,
	rest4b,

	\rest, \rest, p2, \rest,
	\rest, \rest, p2, p2x,
	\rest, p2, \rest, \rest,
	p2, \rest, p2x, p2x,

	rest4b,
	p3x, p3x,
	\rest, \rest, p1,
	p2, p2x
], 1), ~phrases);
pl3 = Psym(Pseq(#[
	// bass1, \rest, \rest,
	// bass1, \rest, \rest,
	// bass1, bass1,
	// bass1, bass2,
	//
	bass2, bass2,
	bass1, bass1,
	bass2, bass2,
	bass1, bass1,

	bass2, bass2,
	bass1, bass1,
	bass1, bass2,
	bass2, bass2,

	bass1, \rest, \rest,
	bass1, \rest, \rest1,
	bass1, bass2,
	bass2, bass2
], 1), ~phrases);

pp = Ppar([pl3, pl2, pl1],64);
pp.render(path: "undercurrent.wav", maxTime: 64.0, headerFormat: "WAV");
