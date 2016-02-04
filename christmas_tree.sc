//Write sounds to file

 ( SynthDef('fullkickdrum’, {
    var subosc, subenv, suboutput, clickosc, clickenv, clickoutput;

    subosc = {SinOsc.ar(60)};
    subenv = {Line.ar(1, 0, 1, doneAction: 2)};

    clickosc = {LPF.ar(WhiteNoise.ar(1),1500)};
    clickenv = {Line.ar(1, 0, 0.02)};

    suboutput = (subosc * subenv);
    clickoutput = (clickosc * clickenv);

    Out.ar(0,
        Pan2.ar(suboutput + clickoutput, 0)
    )

}).send(s);
)

t = Synth('fullkickdrum');

(
var g, o, samplePath, wavFile, oscFile;
g = [
	[0, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]],
	[1, [\n_free, 1000]],
	[1, [\s_new, \NRTsine, 1001, 0, 0, \freq, 660]],
	[2, [\n_free, 1001]],
	[2, [\s_new, \NRTsine, 1002, 0, 0, \freq, 880],
		[\s_new, \NRTsine, 1003, 0, 0, \freq, 220]],
	[3, [\n_free, 1002], [\n_free, 1003]],
	[4, [\c_set, 0, 0]]
];
samplePath = thisProcess.nowExecutingPath.dirname;
oscFile = samplePath +/+ "christmas_tree.osc";
wavFile = samplePath +/+ "christmas_tree.wav";
o = ServerOptions.new.numOutputBusChannels = 2; // stereo output
Score.recordNRT(g, oscFile, wavFile, options: o); // synthesize
)
