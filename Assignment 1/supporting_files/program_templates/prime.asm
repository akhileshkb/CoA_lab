	.data
a:
	5165
	.text
main:
	load %x0, $a, %x3
	srli %x3, 1, %x4
	addi %x0, 1, %x6
loop:
	beq %x4, %x6, success
	div %x3, %x4, %x5
	beq %x31, %x0, endl
	subi %x4, 1, %x4
	jmp loop
success:
	addi %x0, 1, %x10
	end
endl:
	subi %x0, 1, %x10
	end