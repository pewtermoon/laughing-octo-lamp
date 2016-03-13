s.boot;

{Pan2.ar(SinOsc.ar(440,0,0.1),0.0)}.play

s.quit;

{Pan2.ar(SinOsc.ar(MouseX.kr(200,2000),0,0.1),0.0)}.play


{Pan2.ar(SinOsc.ar(440,0,0.1),0.0)}.play

//The most simple SynthDef (this is the basic recipe) \xxxx is the name of the synth
SynthDef(\sine, {Out.ar(0,SinOsc.ar(440,0,0.1))}).add;

//Then to play:
a = Synth(\sine);

//And to stop:
a.free;

//Simpler "on-the-fly" alternative is:
{SinOsc.ar(440,0,0.1)}.play;

//Note that SynthDef(\nameOfSynth,{}).add; is same as
//          SynthDef("nameOfSynth,{}).add; Second is string, 1st is symbol

SynthDef(\sine,{arg freq=440, amp=0.1; Out.ar(0,SinOsc.ar(freq,0,amp))}).add;

//With args
SynthDef(\syn1, {arg freq = 440, amp = 0.1;
	var x;
	x = SinOsc.ar(freq, 0, amp);
	Out.ar(0, x);
}).add

Synth(\syn1,[\freq, 300, \amp, 0.05]);

//////Time-limited sounds
{SinOsc.ar(440,0,Line.kr(0.1,0.0,1.0))}.scope

//If you look at server box, see this 1 actually stops when sound ends
{SinOsc.ar(440,0,Line.kr(0.1,0,1,doneAction:2))}.scope

//Note: envelopes help you to get more control over the shape of the change
//than the simple line control rate (hint: use .plot method to see shape of env
Env([1,0,1],[1,1]).plot

//Env(levels, times). More like vector notation - level is y position, time is how
//far to go until you reach next level, so should always be 1 less time than level
Env([0,1,1,0], [0.1,1,2]).plot //Check it out boy!

//Great, but notice that it's a bit square. There are lots of pre-designed
//Env methods to help you make nicer envs. E.g...
Env.linen(0.03,0.5,0.1).plot  //linen has attackTime, sustainTime, releaseTime, level, curve
Env.adsr(0.01, 0.5, 0.5, 0.1, 1.0, 0).plot  //attackTime, decayTime, sustainLevel, releaseTime, peakLevel, curve
Env.perc(0.05,0.5,1.0,2).plot //arguments attackTime, releaseTime, level, curve: good for percussive hit envelopes

//Need to run envelope through EnvGen and multiple by signal to get it to work
{SinOsc.ar(440,0,0.1)*EnvGen.kr(Env([0,1,1,0],[0.125,0.1,0.25]))}.scope

//Try do the same for frequency - this could actually lead to whole tune!
Env([1000,20],[1.0]).plot

{SinOsc.ar(440,0,0.1)*EnvGen.kr(Env([0,1,1,0],[0.125,0.1,0.25]))}.scope

{SinOsc.ar(
	EnvGen.kr(
		Env(
			[10000,440,800,500,800,10000,300,200],
			[0.5,0.25,0.125,0.125,0.25,0.25,0.5],3,4,0), doneAction:2
	),0,0.1)*
	EnvGen.kr(Env([0,1,1,0],[1,5,1]))
}.scope

//Eg2
{Saw.ar(EnvGen.ar(Env([10000,20],[0.5])),EnvGen.ar(Env([0.1,0],[2.0])))}.scope

//Line and Xline etc. have same deallocation logic as Env/EnvGen:
{Saw.ar(SinOsc.ar(1,0,10,440),XLine.kr(0.0001,1,1,doneAction:2))}.scope

//Can loop envelopes by specifying where you want the loopNode
e = Env([0.0,0.0,1.0,0.0],[0.5,1.0,2.0],0,2,0); //releaseNode at 2, loopNode at 0
a= {arg gate=1; EnvGen.ar(e,gate,doneAction:2)*SinOsc.ar(550,0,0.1)}.play
a.set(\gate, 0); //takes 2.0 seconds to fade out

/////////
(
SynthDef(\event,{ arg freq=440, amp=0.5, pan=0.0;
	var env;
	env = EnvGen.ar(  Env([0,1,1,0],[0.01, 0.1, 0.2]),  doneAction:2);
	Out.ar(0,  Pan2.ar(Blip.ar(freq) * env * amp, pan))
}).add;
)

(
SynthDef(\event,{ arg freq=440, amp=0.5, pan=0.0;
	var env;
	env = EnvGen.ar(  Env([0,1,1,0],[0.01, 0.1, 0.2]),  doneAction:2);
	Out.ar(0,  Pan2.ar(SinOsc.ar(freq) * env * amp, pan))
}).add;
)


//test event sound
Synth(\event)	//accepts defaults. Event shouldn't hang around once envelope finished due to doneAction:2

Synth(\event,[\freq,200,\amp,0.2,\pan,1.0]) //pan 1.0 should send to right ear

//Sequencing
(
{
	Synth(\event);
	1.0.wait;	//wait for 1 time unit of default tempo, will probably work out to 1 second
	Synth(\event);
}.fork; 	//.fork is a computer science term for setting a new process (sequence) in action
)

({
	Synth(\event);
	1.0.wait;
	Synth(\event);
}).fork;

//And here's how to easily repeat things
(
{
	64.do {
		Synth(\event,[\freq, rrand(440,540,640,740), \amp, 0.1]);
		0.25.wait;
	};
}.fork;
)

(
{Pbind(
	\dur, 0.125,
	\freq, Pseq([100,200,300,400],inf)       // try a different list
)}.fork;
)

//Note: difference between a routine (scheduling) and a stream (patterns)

//patterns
(
SynthDef(\alicepavelinstr, {arg out=0, alice=440, pavel=0.5, pan=0.0, gate=1;
			var z;
			z = Resonz.ar(Pulse.ar(alice, pavel),XLine.kr(5000,1000),0.1,5) * Linen.kr(gate, 0.01, 0.1, 0.3, 2);
			Out.ar(out, Pan2.ar(z, pan));
			}
).add
)

(
var clock;
clock = TempoClock(1.5); // tempoclock at 90 bpm
Pbind(
	\alice, Pseq(440*[1,2,3],inf),   // freq
	\pavel, Pseq([0.1,0.5, 0.8],inf),		// pulse width
	\dur, Pseq([0.5,0.25,0.25],inf),				// duration of event in beats
	\legato, 0.5,							// proportion of inter onset time to play
	\instrument, \alicepavelinstr			// your own synthesiser
).play(clock);
)

//Combining scheduling and patterns
(
SynthDef(\pulsepan,{arg freq, amp;
		Out.ar(0,
		SinOsc.ar(freq,0,amp)*(Line.kr(0.5, 0, 0.125, doneAction:2))
			)
}).add
)


//
(
var p, p2, t, p3, p4;
p = Pseq(
	[
	Pseq([1,\,1/3,\]*440,2),
	Pseq([1,2,3,4]*110,2),
	Pseq([\,2,\,4]*220,2),
	Pseq([1,2,3,4]*330,2),
	],8
).asStream;
p2 = p*2.asStream;
p3 = p*3.asStream;
p4 = p*0.5.asStream;
t = {
		loop({
			Synth(\pulsepan,[\freq, p4.next]); // get next frequency value
		0.25.wait;
			Synth(\pulsepan,[\freq, p.next]); // get next frequency value
		0.125.wait;
			Synth(\pulsepan,[\freq, p2.next]);
		0.25.wait; //If there's no wait, will play simultaneously
		    Synth(\pulsepan,[\freq, p3.next]);
		0.125.wait;

		});
	}.fork;
)

///////
p = Pseq([60, 72, 71, 67, 69, 71, 72, 60, 69, 67], 1);
r = p.asStream; //now a routine that can be played...
r.next;
while { (m = r.next).notNil } { m.postln };

////
(
var midi, dur;
midi = Pseq([60, 72, 71, 67, 69, 71, 72, 60, 69, 67], 1).asStream;
dur = Pseq([2, 2, 1, 0.5, 0.5, 1, 1, 2, 2, 3], 1).asStream;

SynthDef(\smooth, { |freq = 440, sustain = 1, amp = 0.5|
    var sig;
    sig = SinOsc.ar(freq, 0, amp) * EnvGen.kr(Env.linen(0.05, sustain, 0.1), doneAction: 2);
    Out.ar(0, sig ! 2)
}).add;

r = Task({
    var delta;
    while {
        delta = dur.next;
        delta.notNil
    } {
        Synth(\smooth, [freq: midi.next.midicps, sustain: delta]);
        delta.yield;
    }
}).play(quant: TempoClock.default.beats + 1.0);
)

/////
//Actually overthinking this. I think I can do everything I want with just
//patterns:
(
SynthDef(\pulsepan,{arg freq, amp;
		Out.ar(0,
		SinOsc.ar(freq,0,amp)*(Line.kr(0.5, 0, 0.125, doneAction:2))
			)
}).add
)

(
var x;
x = Pseq([(1..8)*0.15,(1..8)*0.2,(1..8)*0.1,(1..8)*0.1]*440,2);
p = Pbind(
	\instrument, \pulsepan,
	\freq, Pseq([
			x,
			x*1/2
		],8),
	\dur, 0.25,
	\amp, 0.1
).play;
)

//Stuff with sequences!
(1..10).collect({|n| n.squared });
(1..10).collect({|n| n*0.1});
(1..10)*0.1;

//Triggers
s.boot;
{SinOsc.ar(300+(200*Latch.ar(SinOsc.ar(13.3), Impulse.ar(10))))*0.2}.play

{SinOsc.ar(EnvGen.ar(Env([63,63,60,55,60],0.125!4,curve:\step),Impulse.kr(2)).midicps)}.play

//percussive sound retriggered 3 times a second

(

{

var sound,env, trig;


trig= Impulse.ar(3); //trigger source


sound= Mix(LFPulse.ar(110*[1,5/2],0.0,0.5,0.2));


env= EnvGen.ar(Env.perc(0.02,0.2),trig); //with retriggering controlled by impulse


Pan2.ar(sound*env,0.0)

}.play

)


//


//demonstration of a simple structure for a piece where different sections appear in a desired order

//note if you were extending this to some large-scale form with sectional repeats, that repeated materials could be put into functions, to avoid repetition through encapsulation.



(
{
	SynthDef(\bleep,{ arg out=0, note=60, amp=0.5, pan=0.0;
		var freq, env;
		freq = note.midicps;
		env = EnvGen.ar(
		Env([0,1,1,0],[0.01, 0.1, 0.2]),
		levelScale:amp,
		doneAction:2
		);
		Out.ar(out,
		Pan2.ar(Blip.ar(freq) * env, pan)
		)
	}).add;

	SynthDef(\bleep2,{ arg out=0, note=60, amp=0.5, pan=0.0;
		var freq, env;
		freq = note.midicps;
		env = EnvGen.ar(
		Env([0,1,1,0],[0.1, 0.1, 0.02]),
		levelScale:amp,
		doneAction:2
		);
		Out.ar(out,
		Pan2.ar(Blip.ar(freq, Line.kr(10,1,1)) * env, pan)
		)
	}).add;

	SynthDef(\mlpitch,{
		var soundin, amp, freq, hasFreq;
		soundin= SoundIn.ar;
		amp= Amplitude.kr(soundin);
		#freq, hasFreq= Pitch.kr(soundin);
		Out.ar(0,amp*SinOsc.ar(freq))
	}).add;
	s.sync;  //won't proceed until server confirms it has the SynthDefs ready

	//make buffers;
	//s.sync;
	//three sections
	//1.
	10.do{|i|
		Synth([\bleep, \bleep2].choose);
		0.15.wait;
	};
	1.0.wait;
	//2.
	a= Synth(\mlpitch);
	1.0.wait;
	a.free;
	1.0.wait;
	//3. two sequences of actions happen simultaneously (note no gap between the two)
	{
		100.do{|i|
			Synth([\bleep, \bleep2].choose,[\note, ([60,62,64,66,67,69,71]-12).choose]);
			rrand(0.05,0.15).wait;
		};
	}.fork;
	//though process has just been forked off, straight to do further things in this thread too!
	100.do{|i|
		Synth([\bleep, \bleep2].choose,[\note, [60,62,64,66,67,69,71].choose]);
		0.1.wait;
	};
}.fork;
)

//Dictionary of pattern phrases that can then be called
(
SynthDef(\bass, { |freq = 440, gate = 1, amp = 0.5, slideTime = 0.17, ffreq = 1100, width = 0.15,
        detune = 1.005, preamp = 4|
    var    sig,
        env = Env.adsr(0.01, 0.3, 0.4, 0.1);
    freq = Lag.kr(freq, slideTime);
    sig = Mix(VarSaw.ar([freq, freq * detune], 0, width, preamp)).distort * amp
        * EnvGen.kr(env, gate, doneAction: 2);
    sig = LPF.ar(sig, ffreq);
    Out.ar(0, sig ! 2)
}).add;
)

//
// Uses the bass SynthDef above
(
~phrases = (
    repeated: Pbind(
        \instrument, \bass,
        \midinote, 36,
        \dur, Pseq([0.75, 0.25, 0.25, 0.25, 0.5], 1),
        \legato, Pseq([0.9, 0.3, 0.3, 0.3, 0.3], 1),
        \amp, 0.5, \detune, 1.005
    ),
    octave: Pmono(\bass,
        \midinote, Pseq([36, 48, 36], 1),
        \dur, Pseq([0.25, 0.25, 0.5], 1),
        \amp, 0.5, \detune, 1.005
    ),
    tritone: Pmono(\bass,
        \midinote, Pseq([36, 42, 41, 33], 1),
        \dur, Pseq([0.25, 0.25, 0.25, 0.75], 1),
        \amp, 0.5, \detune, 1.005
    ),
    dim: Pmono(\bass,
        \midinote, Pseq([36, 39, 36, 42], 1),
        \dur, Pseq([0.25, 0.5, 0.25, 0.5], 1),
        \amp, 0.5, \detune, 1.005
    )
);

TempoClock.default.tempo = 128/60;
)

(
// the higher level control pattern is really simple now
p = Psym(Pxrand(#[repeated, octave, tritone, dim], inf), ~phrases).play;
)

(
// the higher level control pattern is really simple now
var p1, p2;
p1 = Psym(Pseq(#[dim, \rest, tritone, \rest], 4), ~phrases);
p2 = Psym(Pseq(#[octave, \rest, repeated, dim], 4), ~phrases);
Ppar([p1,p2],1).play;
)
