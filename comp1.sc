//Synth def
(
SynthDef(\syn1, { |freq = 440, amp = 0.05, scal = 2|
    var sig, env;
	sig = SinOsc.ar(freq, 0, amp);
	env = Env([0,0.9,0.9,0], [0.0125,0.0125,0.5]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).add;
)

//Pattern lib
(
~phrases = (
	p1: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1, \rest, 1.5, 2,
		             1.9, 1.8, 1.7, 1.6]*440, 1),
		\dur, 0.25,
		// \amp, 0.01,
		\scal, 0.1
	),

	p1x: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1, 1.9, 1.9, 1.9,
		             2.2, 2, 2.2, 1.8]*440, 1),
		\dur, 0.25,
		\scal, 0.5
		// \amp, 0.01
	),

	p2: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1.1, \rest, 1.3, \rest, 1.5, \rest, 1.7, \rest]*330, 1),
		\dur, 0.125,
		// \amp, 0.01
	),

		p2x: Pbind(
		\instrument, \syn1,
		\freq, Pseq([1.1, \rest, 1.3, \rest, 1.5, \rest, 1.7, \rest]*330*2, 1),
		\dur, 0.125,
		// \amp, 0.01
	),

	bass1: Pbind(
	\instrument, \syn1,
	\freq, Pseq([1, 0.8, 0.5, 0.4,
			     0.3, 0.6, 0.3, 0.6]*220),
	\dur, 0.25,
		// \amp, 0.01
	),

	bass2: Pbind(
	\instrument, \syn1,
	\freq, Pseq([0.7, 0.8, 0.9, 1,
		         1.2, 0.6, 1, 1.2]*220),
	\dur, 0.25,
		// \amp, 0.01
	),

	bass3: Pbind(
	\instrument, \syn1,
		\freq, Pseq([0.7, \rest, 0.8, \rest, 0.9, \rest, 1, \rest,
		         1.2, \rest, 0.6, \rest, 1, \rest, 1.2]*220),
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
TempoClock.default.tempo = 1;
)

(
// the higher level control pattern is really simple now
var pl1, pl2, pl3;
pl1 = Psym(Pseq(#[
	              rest4b,
	              rest4b,
	              rest4b,
	              rest4b,

	              p1, \rest, \rest,
                  p1x, \rest, \rest,
	              p1, p1x,
                  p1, p1x,

	              p1, \rest, \rest,
                  p1x, \rest, \rest,
	              p1, p1x,
                  p1, p1x

], 1), ~phrases);
pl2 = Psym(Pseq(#[
	              rest4b,
	              rest4b,
	              rest4b,
	              rest4b,

		          rest4b,
	              rest4b,
	              rest4b,
	              rest4b,

	              p2, \rest, \rest,
	              p2x, \rest, \rest,
                  p2, p2x,
	              p2x, p2x], 1), ~phrases);
pl3 = Psym(Pseq(#[
	              bass1, \rest, \rest,
	              bass1, \rest, \rest,
	              bass1, bass1,
	              bass1, bass2,

                  bass1, \rest, \rest,
	              bass1, \rest, \rest,
	              bass1, bass1,
	              bass1, bass2,

                  bass2, bass2,
	              bass1, bass1,
	              bass1, bass2,
	              bass2, bass2

], 1), ~phrases);
Ppar([pl3, pl2, pl1],4).play;
)
