.data
	a : 0
.text
	LD a
	STO 1000
	LDI 2
	STO 1001
	LD 1000
	SUB 1001
fim_main:
	HLT 0