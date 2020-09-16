	.data
n:
	10
	.text
main:
	load %x0, $n, %x7 
	addi %x0, 1, %x4
	addi %x0, 0, %x3
	addi %x0, 65535, %x6
	sub %x6, %x7, %x7
	store %x3, 0, %x6 
	subi %x6, 1, %x6
	store %x4, 0, %x6
	subi %x6, 1, %x6
loop:
	beq %x7, %x6, endl 
	add %x3, %x4, %x5
	store %x5, 0, %x6
	subi %x6, 1, %x6
	add %x4, %x0, %x3
	add %x5, %x0, %x4
	jmp loop
endl:
	end