package com.digitalchina.xa.it.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitalchina.xa.it.service.SigninRewardService;

@Controller
@RequestMapping(value = "/signinReward")
public class SigninRewardController {
	@Autowired
	private SigninRewardService srService;
	
	@ResponseBody
	@GetMapping("/saveRandom")
	public Object saveSigninStatusRandom(
			@RequestParam(name = "itcode", required = true) String itcode){
		return srService.saveSigninInfo(itcode);
	}
	@ResponseBody
	@GetMapping("/saveConstant")
	public Object saveSigninStatusConstant(
			@RequestParam(name = "itcode", required = true) String itcode){
		return srService.saveSigninInfoConstant(itcode);
	}
	@ResponseBody
	@GetMapping("/signinStatus")
	public Object signinStatus(
			@RequestParam(name = "itcode", required = true) String itcode){
		return srService.checkSigninStatus(itcode);
	}
	
	@ResponseBody
	@GetMapping("/addLuckyNumber")
	public Map<String, Object> addLuckyNumber(
			@RequestParam(name = "param", required = true) String param){
		return srService.addLuckyNumber(param);
	}
	
	//NEWCODE START-泛微签到模块的奖励代码-START
	/**
	 * @api {get} /signinReward/chargeToContract/:value 签到合约充值
	 * @apiVersion 0.0.1
	 *
	 * @apiName ChargeToSignInContract
	 * @apiGroup SignIn
	 *
	 * @apiParam {Integer} value 充值金额,正整数.
	 *
	 * @apiSuccess {String} hashcode 充值交易的hash值.
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 *     0x2f58a9a88392d9f5fcdfef155b1a361969d1e5ebf8862f5973be69146bb11348
	 *     
	 * @apiError ChargeFailed 充值失败
	 * @apiErrorExample ChargeFailed-Response:
	 *     HTTP/1.1 200 OK
	 *     error
	 */
	@RequestMapping("/chargeToContract/{value}")
	public String chargeToContract(@PathVariable String value) {
		String result = srService.chargeToContract(value);
		return result;
	}
	
	/**
	 * @api {get} /signinReward/signinReward/:itcode/:reward 发放签到奖励
	 * @apiVersion 0.0.1
	 *
	 * @apiName SignInReward
	 * @apiGroup SignIn
	 *
	 * @apiParam {String} itcode 奖励用户的itcode.
	 * @apiParam {Integer} reward 奖励金额,正整数.
	 *
	 * @apiSuccess {String} hashcode 发放签到奖励交易的hash值.
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 *     0x2f58a9a88392d9f5fcdfef155b1a361969d1e5ebf8862f5973be69146bb11348
	 *     
	 * @apiError SignInRewardFailed-ItcodeNotFound itcode未找到
	 * @apiErrorExample ItcodeNotFound-Response:
	 *     HTTP/1.1 200 OK
	 *     error.Can not find this itcode.
	 *     
	 * @apiError SignInRewardFailed-TransactionException 交易异常
	 * @apiErrorExample TransactionException-Response:
	 *     HTTP/1.1 200 OK
	 *     error.
	 */
	@RequestMapping("/signinReward/{itcode}/{reward}")
	public String signinReward(@PathVariable String itcode, @PathVariable int reward) {
		String result = srService.signinReward(itcode, reward);
		return result;
	}
	
	/**
	 * @api {get} /signinReward/voteReward/:itcode 发放投票奖励
	 * @apiVersion 0.0.1
	 *
	 * @apiName VoteReward
	 * @apiGroup Vote
	 *
	 * @apiParam {String} itcode 奖励用户的itcode.
	 *
	 * @apiSuccess {String} hashcode 发放投票奖励交易的hash值.
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 *     0x2f58a9a88392d9f5fcdfef155b1a361969d1e5ebf8862f5973be69146bb11348
	 *     
	 * @apiError VoteRewardFailed-ItcodeNotFound itcode未找到
	 * @apiErrorExample ItcodeNotFound-Response:
	 *     HTTP/1.1 200 OK
	 *     error.Can not find this itcode.
	 *     
	 * @apiError VoteRewardFailed-TransactionException 交易异常
	 * @apiErrorExample TransactionException-Response:
	 *     HTTP/1.1 200 OK
	 *     error.
	 */
	@RequestMapping("/voteReward/{itcode}")
	public String voteReward(@PathVariable String itcode) {
		String result = srService.voteReward(itcode);
		return result;
	}
	
	/**
	 * @api {get} /signinReward/attendanceReward/:employeeNumber 发放考勤奖励
	 * @apiVersion 0.0.1
	 *
	 * @apiName AttendanceReward
	 * @apiGroup AdditionalReward
	 *
	 * @apiParam {String} employeeNumber 奖励用户的员工编号,多员工编号以","分隔,例如"XXX,XXX,XXX".
	 *
	 * @apiSuccessExample Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@RequestMapping("/attendanceReward/{employeeNumber}")
	public void attendanceReward(@PathVariable String employeeNumber) {
		srService.attendanceReward(employeeNumber);
	}
	//NEWCODE END-泛微签到模块的奖励代码-END
}
