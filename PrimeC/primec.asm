.data
	valor : 0
	a : 0
.text
	JMP main0
teste:
	LD valor
	RETURN 0
main0:
	LD a
	STO $out_port
	HLT 0