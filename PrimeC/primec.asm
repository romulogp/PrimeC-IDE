.data
	a : 0
	vet1 : 0, 0, 0, 0, 0
.text
	LDI 0
	STO a
	LD $in_port
	STO a
	LD a
	STO $out_port
	HLT 0