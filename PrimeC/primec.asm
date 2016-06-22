.data
	a : 0
	vet : 0, 0, 0
.text
	LDI 10
	SUB a
	ADDI 0
	STO $indr
	LDV vet
	STO a
	HLT 0
